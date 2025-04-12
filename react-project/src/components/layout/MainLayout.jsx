import React from "react";
import Sidebar from "./Sidebar";
import ChatArea from "../messages/ChatArea";

const MainLayout = () => {
  return (
    <div className="flex h-screen overflow-hidden">
      <Sidebar />
      <ChatArea />
    </div>
  );
};

export default MainLayout;
