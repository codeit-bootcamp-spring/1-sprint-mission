// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/user/findAll`,
    USER_STATUS: `${API_BASE_URL}/userStatuses/all`,
    BINARY_CONTENT: `${API_BASE_URL}/binaryContents`
};

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

async function fetchAndRenderUsers() {
    try {
        // 사용자 데이터 가져오기
        const usersResponse = await fetch(ENDPOINTS.USERS);
        if (!usersResponse.ok) {
            throw new Error(`Failed to fetch users: ${usersResponse.status}`);
        }
        const users = await usersResponse.json();
        console.log("Users fetched:", users);

        // 사용자 상태 데이터 가져오기
        const userStatusesResponse = await fetch(ENDPOINTS.USER_STATUS);
        if (!userStatusesResponse.ok) {
            throw new Error(`Failed to fetch userStatuses: ${userStatusesResponse.status}`);
        }
        const userStatuses = await userStatusesResponse.json();
        console.log("User statuses fetched:", userStatuses);

        // 프로필 데이터 가져오기
        const profileResponse = await fetch(ENDPOINTS.BINARY_CONTENT);
        if (!profileResponse.ok) {
            throw new Error(`Failed to fetch profiles: ${profileResponse.status}`);
        }
        const profiles = await profileResponse.json();
        console.log("Profiles fetched:", profiles);

        // 사용자 목록 렌더링
        renderUserList(users, userStatuses, profiles);
    } catch (error) {
        console.error('Error fetching users or user statuses:', error);
    }
}

// 사용자 목록 렌더링
async function renderUserList(users, userStatuses, profiles) {
    const userListElement = document.getElementById('userList');
    if (!userListElement) {
        console.error("Error: userList element not found");
        return;
    }

    userListElement.innerHTML = ''; // 기존 내용 삭제

    // 사용자 상태 맵 생성 (ID를 문자열로 변환)
    const userStatusMap = new Map();
    userStatuses.forEach(status => {
        userStatusMap.set(String(status.id), status);
    });
    console.log("User Status Map:", userStatusMap);

    // 프로필 맵 생성 (id → URL 매핑)
    const profileMap = new Map();
    profiles.forEach(profile => {
        profileMap.set(profile.id, `/api/binaryContents/view/${profile.id}`);
    });
    console.log("Profile Map:", profileMap);

    // 사용자 목록 렌더링
    for (const user of users) {
        console.log(`User ${user.name} userStatusId:`, user.userStatusId);
        console.log("User Status Map has userStatusId:", userStatusMap.has(String(user.userStatusId)));

        // 사용자 상태 가져오기
        const userStatus = userStatusMap.get(String(user.userStatusId));
        console.log(`User ${user.name} status:`, userStatus);
        console.log(`User ${user.name} online value:`, userStatus ? userStatus.online : "N/A");

        const isOnline = userStatus ? userStatus.online : false; // 기본값: 오프라인

        // 프로필 이미지 URL 가져오기
        console.log(`User ${user.name} profileId:`, user.profileImageId);
        console.log("Profile Map has profileId:", profileMap.has(user.profileImageId));

        const profileUrl = profileMap.get(user.profileImageId) || '/default-avatar.png';
        console.log(`Final profileUrl for ${user.name}:`, profileUrl);

        const userElement = document.createElement('div');
        userElement.className = 'user-item';
        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.name}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.name}</div>
                <div class="user-email">${user.email}</div>
            </div>
            <div class="status-badge ${isOnline ? 'online' : 'offline'}">
                ${isOnline ? '온라인' : '오프라인'}
            </div>
        `;

        userListElement.appendChild(userElement);
    }
}
