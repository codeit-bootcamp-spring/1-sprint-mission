import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import useUserListStore from './userListStore';
import { updateUser } from '../api/user';
import { UserDto } from '../types/api';

interface UserStore {
  currentUserId: string | null;
  setCurrentUser: (user: UserDto) => void;
  logout: () => void;
  updateUser: (userId: string, formData: FormData) => Promise<UserDto>;
}

const useUserStore = create<UserStore>()(
  persist(
    (set) => ({
      currentUserId: null,
      setCurrentUser: (user) => set({ currentUserId: user.id }),
      logout: () => {
        const currentUserId = useUserStore.getState().currentUserId;
        if (currentUserId) {
          useUserListStore.getState().updateUserStatus(currentUserId);
        }
        set({ currentUserId: null });
      },
      updateUser: async (userId, formData) => {
        try {
          const userData = await updateUser(userId, formData);
          await useUserListStore.getState().fetchUsers();
          return userData;
        } catch (error) {
          console.error('사용자 정보 수정 실패:', error);
          throw error;
        }
      },
    }),
    {
      name: 'user-storage',
      storage: createJSONStorage(() => sessionStorage),
    }
  )
);

export default useUserStore; 