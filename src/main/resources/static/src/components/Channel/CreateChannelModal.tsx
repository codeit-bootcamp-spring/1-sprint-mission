import React, { useMemo, useState } from 'react';
import defaultProfile from '@/assets/default_profile.png';
import useChannelStore from '@/stores/channelStore';
import useUserListStore from '@/stores/userListStore';
import useUserStore from '@/stores/userStore';
import {
  Checkbox,
  CloseButton,
  CreateButton,
  Description,
  ErrorMessage,
  Form,
  Input,
  InputGroup,
  Label,
  ModalContainer,
  ModalContent,
  ModalHeader,
  ModalOverlay,
  ModalTitle,
  NoResults,
  SearchInput,
  UserAvatar,
  UserEmail,
  UserInfo,
  UserItem,
  UserList,
  UserName
} from './styles';
import useBinaryContentStore from '@/stores/binaryContentStore';
import { ChannelDto, PublicChannelCreateRequest } from '@/types/api';

interface CreateChannelModalProps {
  isOpen: boolean;
  type: 'PUBLIC' | 'PRIVATE' | null;
  onClose: () => void;
  onCreateSuccess: (channelData: ChannelDto) => void;
}

interface ChannelFormData {
  name: string;
  description: string;
}

function CreateChannelModal({ isOpen, type, onClose, onCreateSuccess }: CreateChannelModalProps): JSX.Element | null {
  const [channelData, setChannelData] = useState<ChannelFormData>({
    name: '',
    description: ''
  });

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedUsers, setSelectedUsers] = useState<string[]>([]);
  const [error, setError] = useState('');

  // userListStore에서 사용자 목록 가져오기
  const users = useUserListStore((state) => state.users);
  const binaryContents = useBinaryContentStore((state) => state.binaryContents);
  const currentUserId = useUserStore((state) => state.currentUserId);

  const filteredUsers = useMemo(() => {
    return users
    .filter(user => user.id !== currentUserId)
    .filter(user => 
      user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.email.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [searchTerm, users, currentUserId]);

  const createPublicChannel = useChannelStore((state) => state.createPublicChannel);
  const createPrivateChannel = useChannelStore((state) => state.createPrivateChannel);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setChannelData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const toggleUserSelection = (userId: string) => {
    setSelectedUsers(prev => {
      if (prev.includes(userId)) {
        return prev.filter(id => id !== userId);
      }
      return [...prev, userId];
    });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');

    try {
      let createdChannel;
      if (type === 'PUBLIC') {
        if (!channelData.name.trim()) {
          setError('채널 이름을 입력해주세요.');
          return;
        }
        const publicChannelData: PublicChannelCreateRequest = {
          name: channelData.name,
          description: channelData.description
        };
        createdChannel = await createPublicChannel(publicChannelData);
      } else {
        if (selectedUsers.length === 0) {
          setError('대화 상대를 선택해주세요.');
          return;
        }
        // 현재 사용자를 participants에 포함
        const participantIds = currentUserId && [...selectedUsers, currentUserId] || selectedUsers;
        createdChannel = await createPrivateChannel(participantIds);
      }
      onCreateSuccess(createdChannel);
    } catch (error: any) {
      console.error('채널 생성 실패:', error);
      setError(
        error.response?.data?.message || 
        '채널 생성에 실패했습니다. 다시 시도해주세요.'
      );
    }
  };

  if (!isOpen) return null;

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContainer onClick={e => e.stopPropagation()}>
        <ModalHeader>
          <ModalTitle>
            {type === 'PUBLIC' ? '채널 만들기' : '개인 메시지 시작하기'}
          </ModalTitle>
          <CloseButton onClick={onClose}>&times;</CloseButton>
        </ModalHeader>
        <ModalContent>
          <Form onSubmit={handleSubmit}>
            {error && <ErrorMessage>{error}</ErrorMessage>}
            {type === 'PUBLIC' ? (
              <>
                <InputGroup>
                  <Label>채널 이름</Label>
                  <Input
                    name="name"
                    value={channelData.name}
                    onChange={handleChange}
                    placeholder="새로운-채널"
                    required
                  />
                </InputGroup>
                <InputGroup>
                  <Label>채널 설명</Label>
                  <Description>
                    이 채널의 주제를 설명해주세요.
                  </Description>
                  <Input
                    name="description"
                    value={channelData.description}
                    onChange={handleChange}
                    placeholder="채널 설명을 입력하세요"
                  />
                </InputGroup>
              </>
            ) : (
              <InputGroup>
                <Label>사용자 검색</Label>
                <SearchInput
                  type="text"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="사용자명 또는 이메일로 검색"
                />
                <UserList>
                  {filteredUsers.length > 0 ? (
                    filteredUsers.map(user => (
                      <UserItem key={user.id}>
                        <Checkbox
                          type="checkbox"
                          checked={selectedUsers.includes(user.id)}
                          onChange={() => toggleUserSelection(user.id)}
                        />
                        {user.profile ? (
                          <UserAvatar src={binaryContents[user.profile.id].url} />
                        ) : (
                          <UserAvatar src={defaultProfile} />
                        )}
                        <UserInfo>
                          <UserName>{user.username}</UserName>
                          <UserEmail>{user.email}</UserEmail>
                        </UserInfo>
                      </UserItem>
                    ))
                  ) : (
                    <NoResults>검색 결과가 없습니다.</NoResults>
                  )}
                </UserList>
              </InputGroup>
            )}
            <CreateButton type="submit">
              {type === 'PUBLIC' ? '채널 만들기' : '대화 시작하기'}
            </CreateButton>
          </Form>
        </ModalContent>
      </ModalContainer>
    </ModalOverlay>
  );
}

export default CreateChannelModal; 