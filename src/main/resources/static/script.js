// API endpoints
const API_BASE_URL = "/api";
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users`,  // ✅ 올바른 엔드포인트 수정
    BINARY_CONTENT: `${API_BASE_URL}/binaryContent/find`
};

// Initialize the application
document.addEventListener("DOMContentLoaded", () => {
    fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error(`Failed to fetch users: ${response.status}`);

        const users = await response.json();
        console.log("✅ 사용자 목록 가져오기 성공:", users);
        renderUserList(users);
    } catch (error) {
        console.error("🚨 사용자 목록 가져오기 실패:", error);
    }
}

// Fetch user profile image
async function fetchUserProfile(profileImageId) {
    try {
        if (!profileImageId) return "/static/default-avatar.png"; // ✅ 기본 프로필 이미지 경로

        const response = await fetch(`${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileImageId}`);
        if (!response.ok) throw new Error(`Failed to fetch profile: ${response.status}`);

        const profile = await response.json();
        console.log("✅ 프로필 이미지 가져오기 성공:", profileImageId);

        // Convert base64 encoded bytes to data URL
        return `data:${profile.contentType};base64,${profile.bytes}`;
    } catch (error) {
        console.error("🚨 프로필 이미지 가져오기 실패:", error);
        return "/static/default-avatar.png"; // ✅ 오류 발생 시 기본 아바타 사용
    }
}

// Render user list
async function renderUserList(users) {
    const userListElement = document.getElementById("userList");
    userListElement.innerHTML = ""; // 기존 내용 초기화

    for (const user of users) {
        const userElement = document.createElement("div");
        userElement.className = "user-item";

        // Get profile image URL
        const profileUrl = await fetchUserProfile(user.profileImageId);

        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.username}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.username}</div>
                <div class="user-email">${user.email}</div>
            </div>
            <div class="status-badge ${user.online ? "online" : "offline"}">
                ${user.online ? "온라인" : "오프라인"}
            </div>
        `;

        userListElement.appendChild(userElement);
    }
}
