import { create } from 'zustand';
import { genBinaryContentUrl, getBinaryContent } from '../api/binaryContent';

export interface BinaryContentInfo {
  url: string;
  contentType: string;
  fileName: string;
  size: number;
}

interface BinaryContentStore {
  binaryContents: Record<string, BinaryContentInfo>;
  fetchBinaryContent: (id: string) => Promise<BinaryContentInfo | null>;
}

const useBinaryContentStore = create<BinaryContentStore>((set, get) => ({
  binaryContents: {},
  fetchBinaryContent: async (id) => {
    // 이미 가져온 정보가 있다면 재사용
    if (get().binaryContents[id]) {
      return get().binaryContents[id];
    }

    try {
      const binaryContent = await getBinaryContent(id);
      const { contentType, fileName, size  } = binaryContent;
      const url = genBinaryContentUrl(id);
      
      const binaryContentInfo: BinaryContentInfo = {
        url,
        contentType,
        fileName,
        size
      };

      set((state) => ({
        binaryContents: {
          ...state.binaryContents,
          [id]: binaryContentInfo
        }
      }));

      return binaryContentInfo;
    } catch (error) {
      console.error('첨부파일 정보 조회 실패:', error);
      return null;
    }
  }
}));

export default useBinaryContentStore; 