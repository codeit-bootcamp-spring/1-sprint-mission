// API endpoints
const API_BASE_URL = "http://localhost:8080/api";  // 백엔드 URL
const ENDPOINTS = {
  USERS: `${API_BASE_URL}/users`,
  BINARY_CONTENT: `${API_BASE_URL}/binaryContent/find`
};

// Initialize the application
document.addEventListener("DOMContentLoaded", () => {
  fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
  const userListElement = document.getElementById("userList");
  userListElement.innerHTML = "<p>⏳ 사용자 목록을 불러오는 중...</p>"; // 로딩 메시지

  try {
    const response = await fetch(ENDPOINTS.USERS, {
      method: "GET",
      headers: {
        "Content-Type": "application/json"
      }
    });

    if (!response.ok) {
      throw new Error("Failed to fetch users");
    }

    const users = await response.json();
    userListElement.innerHTML = ""; // 기존 내용 초기화
    renderUserList(users);
  } catch (error) {
    console.error("Error fetching users:", error);
    userListElement.innerHTML = `<p style="color: red;">❌ 사용자 목록을 불러오는 데 실패했습니다.</p>`;
  }
}

// Fetch user profile image
async function fetchUserProfile(profileId) {
  try {
    const response = await fetch(
        `${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileId}`, {
          method: "GET"
        });

    if (!response.ok) {
      throw new Error("Failed to fetch profile");
    }
    const profile = await response.json();

    // Convert base64 encoded bytes to data URL
    return `data:${profile.contentType};base64,${profile.bytes}`;
  } catch (error) {
    console.error("Error fetching profile:", error);
    return "/default-avatar.png"; // Default avatar fallback
  }
}

// Render User List
async function renderUserList(users) {
  const userListElement = document.getElementById("userList");
  userListElement.innerHTML = ""; // Clear existing content

  for (const user of users) {
    const userElement = document.createElement("div");
    userElement.className = "user-item";

    // Get profile image URL
    const profileUrl = user.profileId
        ? await fetchUserProfile(user.profileId)
        : "/default-avatar.png";

    userElement.innerHTML = `
            <div class="user-avatar-container">
                <img src="${profileUrl}" alt="${user.username}" class="user-avatar">
            </div>
            <div class="user-info">
                <div class="user-name">${user.username}</div>
                <div class="user-email">${user.userEmail || "이메일 없음"}</div>
            </div>
            <div class="status-badge ${user.online ? "online" : "offline"}">
                ${user.online ? "🟢 ONLINE" : "⚪ OFFLINE"}
            </div>
        `;
    userListElement.appendChild(userElement);
  }
}
