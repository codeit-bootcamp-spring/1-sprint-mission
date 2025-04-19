import React from "react";

const UserStatus = ({ isOnline, className = "" }) => {
  return (
    <div
      className={`h-3 w-3 rounded-full border-2 border-gray-800 ${
        isOnline ? "bg-green-500" : "bg-gray-500"
      } ${className}`}
      title={isOnline ? "온라인" : "오프라인"}
    />
  );
};

export default UserStatus;
