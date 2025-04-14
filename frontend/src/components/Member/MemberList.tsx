import useUserListStore from '@/stores/userListStore';
import useUserStore from '@/stores/userStore';
import {UserDto} from '@/types/api';
import {useEffect} from 'react';
import MemberItem from './MemberItem';
import {MemberHeader, StyledMemberList} from './styles';

function MemberList(): JSX.Element {
  const users = useUserListStore((state) => state.users);
  const fetchUsers = useUserListStore((state) => state.fetchUsers);
  const currentUserId = useUserStore((state) => state.currentUserId);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  // 멤버 정렬: 내 계정 > 온라인 상태 > 사용자명
  const sortedUsers = [...users].sort((a: UserDto, b: UserDto) => {
    // 내 계정을 최상단에 배치
    if (a.id === currentUserId) return -1;
    if (b.id === currentUserId) return 1;

    // 온라인 상태로 정렬
    if (a.online && !b.online) return -1;
    if (!a.online && b.online) return 1;

    // 사용자명으로 정렬
    return a.username.localeCompare(b.username);
  });

  return (
      <StyledMemberList>
        <MemberHeader>멤버 목록 - {users.length}</MemberHeader>
        {sortedUsers.map(user => (
            <MemberItem key={user.id} member={user} />
        ))}
      </StyledMemberList>
  );
}

export default MemberList; 