document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const channelId = params.get('id');

    if (channelId) {
        fetchChannelMessages(channelId);
    }
});

async function fetchChannelMessages(channelId) {
    try {
        const response = await fetch(`/message/json?channelId=${channelId}`);
        const messages = await response.json();
        renderMessages(messages);
    } catch (error) {
        console.error('채팅 메시지를 불러오는 중 오류 발생:', error);
    }
}

function renderMessages(messages) {
    const chatMessages = document.getElementById('chatMessages');
    chatMessages.innerHTML = '';

    messages.forEach(message => {
        const messageItem = document.createElement('div');
        messageItem.classList.add('message-item');
        messageItem.innerHTML = `<strong>${message.senderName}:</strong> ${message.content}`;
        chatMessages.appendChild(messageItem);
    });
}

function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const message = messageInput.value;
    if (!message) return;

    // 메시지 전송 API 호출
    fetch(`/message/1234`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({content: message})
    }).then(() => {
        messageInput.value = '';
        fetchChannelMessages(1234);
    }).catch(error => console.error('메시지 전송 실패:', error));
}
