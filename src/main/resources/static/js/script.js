// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/user`,
    BINARY_CONTENT: `${API_BASE_URL}/file/profile`
};

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('Failed to fetch users');
        const users = await response.json();
        renderUserList(users.data);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

// Fetch user profile image
async function fetchUserProfile(referenceId) {
    try {
        const response = await fetch(`${ENDPOINTS.BINARY_CONTENT}?referenceId=${referenceId}`);
        if (!response.ok) throw new Error('Failed to fetch profile');

        const blob = await response.blob(); // ✅ 바이너리 데이터로 변환
        const objectURL = URL.createObjectURL(blob); // ✅ Blob을 URL로 변환

        // Convert base64 encoded bytes to data URL
        return objectURL;
    } catch (error) {
        console.error('Error fetching profile:', error);
        return '/default-profile.png'; // Fallback to default avatar
    }
}

// Render user list
async function renderUserList(users) {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = ''; // Clear existing content

    for (const user of users) {
        const userElement = document.createElement('div');
        userElement.className = 'user-item';

        // Get profile image URL
        const profileUrl = user.uuid ?
            await fetchUserProfile(user.uuid) :
            '/default-avatar.png';

        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.username.name}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.username.name}</div>
                <div class="user-email">${user.email.email}</div>
            </div>
            <div class="status-badge ${user.online ? 'online' : 'offline'}">
                ${user.online ? '온라인' : '오프라인'}
            </div>
        `;

        userListElement.appendChild(userElement);
    }
}