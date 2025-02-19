// API 엔드포인트
const API_BASE_URL ='http://localhost:8080'; // 실제 API URL을 넣어야 합니다
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users`,
    CHANNELS: `${API_BASE_URL}/channels/all`,
    USER_STATUS: (id) => `${API_BASE_URL}/users/${id}/status`,
    CHANNEL_STATUS: (id) => `${API_BASE_URL}/channels/${id}/status`,
};

// 페이지네이션 관련 변수
let currentPage = 1; // 현재 페이지 (기본값: 1)
const itemsPerPage = 3; // 페이지당 항목 수

// DOMContentLoaded 시, 사용자 목록과 채널 목록을 모두 불러오기
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
    fetchAndRenderChannels();
});

// 사용자 목록을 API에서 가져오는 함수
async function fetchAndRenderUsers() {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = '<p>로딩 중...</p>'; // 로딩 메시지

    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('사용자 목록을 불러오는 데 실패했습니다.');

        const users = await response.json();
        if (users.length === 0) {
            userListElement.innerHTML = '<p>등록된 사용자가 없습니다.</p>';
        } else {
            renderUserList(users);
        }
    } catch (error) {
        console.error('사용자 목록을 불러오는 데 오류 발생:', error);
        userListElement.innerHTML = '<p>사용자 목록을 불러올 수 없습니다.</p>';
    }
}

// 채널 목록을 API에서 가져오는 함수
async function fetchAndRenderChannels() {
    const channelListElement = document.getElementById('channelList');
    channelListElement.innerHTML = '<p>로딩 중...</p>'; // 로딩 메시지

    try {
        const response = await fetch(ENDPOINTS.CHANNELS);
        if (!response.ok) throw new Error('채널 목록을 불러오는 데 실패했습니다.');

        const channels = await response.json();

        if (channels.length === 0) {
            channelListElement.innerHTML = '<p>등록된 채널이 없습니다.</p>';
        } else {
            renderChannelList(channels);
        }
    } catch (error) {
        console.error('채널 목록을 불러오는 데 오류 발생:', error);
        channelListElement.innerHTML = '<p>채널 목록을 불러올 수 없습니다.</p>';
    }
}

async function fetchImageData(profileId) {
    const response = await fetch(`http://localhost:8080/binary-content/${profileId}`);
    const imageBlob = await response.blob();
    const imageUrl = URL.createObjectURL(imageBlob);

    return imageUrl;
}
// 사용자 목록 렌더링 함수
async function renderUserList(users) {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = ''; // 기존 내용 초기화

    // 사용자 목록 렌더링 (5개씩 나누기)
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const usersToDisplay = users.slice(startIndex, endIndex);

    for (const user of usersToDisplay) {
        const userElement = document.createElement('div');
        userElement.className = 'user-item';

        // 기본 프로필 이미지 URL
        let profileImage = 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y&s=50';

        if (user.profileId) {
            // 이미지 데이터가 비동기적으로 로드되므로 await를 사용하여 처리
            profileImage = await fetchImageData(user.profileId);
        }

        userElement.innerHTML = `
            <div class="user-info">
                <img src="${profileImage}" alt="${user.name}'s profile" class="profile-image" />
                <div class="user-datails">
                    <div class="user-name">${user.name}</div>
                    <div class="user-email">${user.email}</div>
                </div>
            </div>
            <div class="status-badge ${user.online ? 'online' : 'offline'}" data-user-id="${user.id}">
                ${user.online ? '🟢 온라인' : '🔴 오프라인'}
            </div>
        `;

        // 상태 변경 이벤트
        const statusBadge = userElement.querySelector('.status-badge');
        statusBadge.addEventListener('click', () => toggleStatus(user.id, user.online, statusBadge, 'user'));

        userListElement.appendChild(userElement);
    }

    // 페이지네이션 버튼 생성
    renderPaginationButtons(users.length);
}

// 채널 목록 렌더링 함수 (5개씩 나누어 렌더링)
function renderChannelList(channels) {
    const channelListElement = document.getElementById('channelList');
    channelListElement.innerHTML = ''; // 기존 내용 초기화

    // 채널을 5개씩 나누기
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const channelsToDisplay = channels.slice(startIndex, endIndex);

    for (const channel of channelsToDisplay) {
        const channelElement = document.createElement('div');
        channelElement.className = 'channel-item';

        // 채널 타입에 따른 구분 (public 또는 private)
        const channelType = channel.type === 'PUBLIC' ? '🔵 공개 채널' : '🟢 비공개 채널';

        channelElement.innerHTML = `
            <div class="channel-info">
                <div class="channel-type">${channelType}</div>
                <div class="channel-name">${channel.channelName || '채널 이름 없음'}</div>
                <div class="channel-description">${channel.description || "NO description"}</div>
            </div>
        `;

        channelListElement.appendChild(channelElement);
    }

    // 페이지네이션 버튼 생성
    renderPaginationButtons(channels.length);
}

// 페이지네이션 버튼 렌더링 함수
function renderPaginationButtons(totalItems) {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = ''; // 기존 버튼 초기화

    const totalPages = Math.ceil(totalItems / itemsPerPage);

    // "이전" 버튼
    if (currentPage > 1) {
        const prevButton = document.createElement('button');
        prevButton.textContent = '◀ 이전';
        prevButton.addEventListener('click', () => {
            currentPage--;
            fetchAndRenderUsers(); // 사용자 목록 리렌더링
            fetchAndRenderChannels(); // 채널 목록 리렌더링
        });
        paginationElement.appendChild(prevButton);
    }

    // "다음" 버튼
    if (currentPage < totalPages) {
        const nextButton = document.createElement('button');
        nextButton.textContent = '다음 ▶';
        nextButton.addEventListener('click', () => {
            currentPage++;
            fetchAndRenderUsers(); // 사용자 목록 리렌더링
            fetchAndRenderChannels(); // 채널 목록 리렌더링
        });
        paginationElement.appendChild(nextButton);
    }
}
