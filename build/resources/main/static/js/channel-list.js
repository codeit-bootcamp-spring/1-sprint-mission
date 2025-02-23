document.addEventListener('DOMContentLoaded', () => {
    fetchChannels();
});

async function fetchChannels() {
    try {
        const response = await fetch('/channel/json?userId=1234');
        const channels = await response.json();
        renderChannels(channels);
    } catch (error) {
        console.error('채널 정보를 불러오는 중 오류 발생:', error);
    }
}

function renderChannels(channels) {
    const channelList = document.getElementById('channelList');
    channelList.innerHTML = '';

    channels.forEach(channel => {
        const channelItem = document.createElement('div');
        channelItem.classList.add('channel-item');
        channelItem.innerHTML = `<a href="/channel.html?id=${channel.id}">${channel.name}</a>`;
        channelList.appendChild(channelItem);
    });
}
