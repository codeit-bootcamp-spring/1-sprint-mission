import defaultProfile from '@/assets/default_profile.png';
import useBinaryContentStore from '@/stores/binaryContentStore';
import { UserDto } from '@/types/api';
import { useEffect, useState } from 'react';
import { Avatar, StatusDot } from '../../styles/common';
import EditUserModal from './EditUserModal';
import { IconButton, StyledUserPanel, UserAvatarContainer, UserControls, UserInfo, UserName, UserStatus } from './styles';

interface UserPanelProps {
  user: UserDto;
}

function UserPanel({ user }: UserPanelProps): JSX.Element {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const {binaryContents, fetchBinaryContent} = useBinaryContentStore();

  useEffect(() => {
    if (user.profile?.id && !binaryContents[user.profile.id]) {
      fetchBinaryContent(user.profile.id);
    }
  }, [user.profile, binaryContents, fetchBinaryContent]);

  return (
    <>
      <StyledUserPanel>
        <UserAvatarContainer>
          <Avatar 
            src={user.profile?.id ? binaryContents[user.profile.id]?.url : defaultProfile} 
            alt={user.username} 
          />
          <StatusDot $online={true} />
        </UserAvatarContainer>
        <UserInfo>
          <UserName>{user.username}</UserName>
          <UserStatus>온라인</UserStatus>
        </UserInfo>
        <UserControls>
          <IconButton onClick={() => setIsEditModalOpen(true)}>⚙️</IconButton>
        </UserControls>
      </StyledUserPanel>

      <EditUserModal 
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        user={user}
      />
    </>
  );
}

export default UserPanel;