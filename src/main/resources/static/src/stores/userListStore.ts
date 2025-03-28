import { create } from 'zustand';
import { getUsers, updateUserStatus } from '../api/user';
import { UserDto } from '../types/api';


interface UserListStore {
  users: UserDto[];
  fetchUsers: () => Promise<void>;
  updateUserStatus: (userId: string) => Promise<void>;
}

const useUserListStore = create<UserListStore>((set) => ({
  users: [],
  fetchUsers: async () => {
    try {
      const users = await getUsers();
      set({users});
    } catch (error) {
      console.error('사용자 목록 조회 실패:', error);
    }
  },
  updateUserStatus: async (userId) => {
    try {
      await updateUserStatus(userId);
    } catch (error) {
      console.error('사용자 상태 업데이트 실패:', error);
    }
  },
}));

export default useUserListStore; 