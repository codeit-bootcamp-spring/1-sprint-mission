import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
        // 필요 시 rewrite 설정 가능
        // rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});