import useChannelStore from '@/stores/channelStore';
import useReadStatusStore from '@/stores/readStatusStore';
import { ChannelDto, UserDto } from '@/types/api';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import UserPanel from '../User/UserPanel';
import { ChannelHeader } from './ChannelHeader.tsx';
import { ChannelItem } from './ChannelItem.tsx';
import CreateChannelModal from './CreateChannelModal.tsx';
import {
    AddChannelButton,
    ChannelScroll,
    ChannelSection,
    ChannelSectionContent,
    ChannelSectionHeader,
    FoldIcon,
    StyledChannelList
} from './styles';

interface ChannelListProps {
  currentUser: UserDto;
  activeChannel: ChannelDto | null;
  onChannelSelect: (channel: ChannelDto) => void;
}

interface FoldedSections {
  PUBLIC: boolean;
  PRIVATE: boolean;
}

interface CreateModalConfig {
  isOpen: boolean;
  type: 'PUBLIC' | 'PRIVATE' | null;
}

function ChannelList({ currentUser, activeChannel, onChannelSelect }: ChannelListProps): JSX.Element {
  const [foldedSections, setFoldedSections] = useState<FoldedSections>({
    PUBLIC: false,
    PRIVATE: false
  });

  const [createModalConfig, setCreateModalConfig] = useState<CreateModalConfig>({
    isOpen: false,
    type: null
  });

  const channels = useChannelStore((state) => state.channels);
  const fetchChannels = useChannelStore((state) => state.fetchChannels);
  const startPolling = useChannelStore((state) => state.startPolling);
  const stopPolling = useChannelStore((state) => state.stopPolling);

  const fetchReadStatuses = useReadStatusStore((state) => state.fetchReadStatuses);
  const updateReadStatus = useReadStatusStore((state) => state.updateReadStatus);
  const hasUnreadMessages = useReadStatusStore((state) => state.hasUnreadMessages);

  useEffect(() => {
    if (currentUser) {
      fetchChannels(currentUser.id);
      fetchReadStatuses();
      startPolling(currentUser.id);

      return () => {
        stopPolling();
      };
    }
  }, [currentUser, fetchChannels, fetchReadStatuses, startPolling, stopPolling]);

  const toggleSection = (sectionName: 'PUBLIC' | 'PRIVATE') => {
    setFoldedSections(prev => ({
      ...prev,
      [sectionName]: !prev[sectionName]
    }));
  };

  const openCreateModal = (type: 'PUBLIC' | 'PRIVATE', e: React.MouseEvent) => {
    e.stopPropagation();
    setCreateModalConfig({
      isOpen: true,
      type
    });
  };

  const closeCreateModal = () => {
    setCreateModalConfig({
      isOpen: false,
      type: null
    });
  };

  const handleCreateChannel = async (channelData: ChannelDto) => {
    try {
      const channels = await fetchChannels(currentUser.id);
      const createdChannel = channels.find(channel => channel.id === channelData.id);
      if (createdChannel) {
        onChannelSelect(createdChannel);
      }
      closeCreateModal();
    } catch (error) {
      console.error('채널 생성 실패:', error);
    }
  };

  const handleChannelSelect = (channel: ChannelDto) => {
    onChannelSelect(channel);
    updateReadStatus(channel.id);
  };

  const groupedChannels = channels.reduce<Record<string, ChannelDto[]>>((acc, channel) => {
    if (!acc[channel.type]) {
      acc[channel.type] = [];
    }
    acc[channel.type].push(channel);
    return acc;
  }, {});

  return (
    <StyledChannelList>
      <ChannelHeader />
      <ChannelScroll>
        <ChannelSection>
          <ChannelSectionHeader onClick={() => toggleSection('PUBLIC')}>
            <FoldIcon $folded={foldedSections.PUBLIC}>▼</FoldIcon>
            <span>일반 채널</span>
            <AddChannelButton onClick={(e) => openCreateModal('PUBLIC', e)}>+</AddChannelButton>
          </ChannelSectionHeader>
          <ChannelSectionContent $folded={foldedSections.PUBLIC}>
            {groupedChannels.PUBLIC?.map(channel => (
              <ChannelItem 
                key={channel.id} 
                channel={channel}
                isActive={activeChannel?.id === channel.id}
                hasUnread={hasUnreadMessages(channel.id, channel.lastMessageAt)}
                onClick={() => handleChannelSelect(channel)}
              />
            ))}
          </ChannelSectionContent>
        </ChannelSection>

        <ChannelSection>
          <ChannelSectionHeader onClick={() => toggleSection('PRIVATE')}>
            <FoldIcon $folded={foldedSections.PRIVATE}>▼</FoldIcon>
            <span>개인 메시지</span>
            <AddChannelButton onClick={(e) => openCreateModal('PRIVATE', e)}>+</AddChannelButton>
          </ChannelSectionHeader>
          <ChannelSectionContent $folded={foldedSections.PRIVATE}>
            {groupedChannels.PRIVATE?.map(channel => (
              <ChannelItem 
                key={channel.id} 
                channel={channel}
                isActive={activeChannel?.id === channel.id}
                hasUnread={hasUnreadMessages(channel.id, channel.lastMessageAt)}
                onClick={() => handleChannelSelect(channel)}
              />
            ))}
          </ChannelSectionContent>
        </ChannelSection>
      </ChannelScroll>
      
      <UserPanelContainer>
        <UserPanel user={currentUser} />
      </UserPanelContainer>
      
      <CreateChannelModal 
        isOpen={createModalConfig.isOpen}
        type={createModalConfig.type}
        onClose={closeCreateModal}
        onCreateSuccess={handleCreateChannel}
      />
    </StyledChannelList>
  );
}

const UserPanelContainer = styled.div`
  margin-top: auto;
  border-top: 1px solid ${({ theme }) => theme.colors.border.primary};
  background-color: ${({ theme }) => theme.colors.background.tertiary};
`;

export default ChannelList; 