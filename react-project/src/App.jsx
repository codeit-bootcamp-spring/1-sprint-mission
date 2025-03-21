import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

// 백엔드 API 주소 (프록시 사용 시 '/api' 로 설정)
const API_BASE_URL = '/api';

/* 1) 로그인 폼 */
function LoginForm({ onLoginSuccess, onGoRegister }) {
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    try {
      // 백엔드 로그인 엔드포인트 (예: POST /api/login)
      // name과 password로 요청
      const res = await axios.post(`${API_BASE_URL}/login`, {
        name,
        password,
      });
      // 로그인 성공 시, 유저 정보(또는 토큰 등) 반환
      onLoginSuccess(res.data);
    } catch (err) {
      setError(err.response?.data?.message || '로그인 실패');
    }
  };

  return (
      <div className="form-container">
        <h2>로그인</h2>
        {error && <p className="error">{error}</p>}
        <form onSubmit={handleLogin}>
          <input
              type="text"
              placeholder="사용자명"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
          />
          <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
          />
          <button type="submit">로그인</button>
        </form>
        <p style={{ marginTop: '10px' }}>
          계정이 없나요?{' '}
          <span
              style={{ color: '#3ba55d', cursor: 'pointer' }}
              onClick={onGoRegister}
          >
          회원가입
        </span>
        </p>
      </div>
  );
}

/* 2) 회원가입 폼 */
function RegistrationForm({ onRegistered, onGoLogin }) {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState('');

  const handleFileChange = (e) => {
    setProfile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const formData = new FormData();
      const userCreateRequest = JSON.stringify({ email, username, password });
      // 백엔드에서 @RequestPart("createRequestDto")로 받는다고 가정
      formData.append(
          'createRequestDto',
          new Blob([userCreateRequest], { type: 'application/json' })
      );
      if (profile) {
        formData.append('profile', profile);
      }
      // POST /api/users 로 회원가입
      const res = await axios.post(`${API_BASE_URL}/users`, formData);
      onRegistered(res.data);
    } catch (err) {
      setError(err.response?.data?.message || '회원가입에 실패했습니다.');
    }
  };

  return (
      <div className="form-container">
        <h2>회원가입</h2>
        {error && <p className="error">{error}</p>}
        <form onSubmit={handleSubmit}>
          <input
              type="email"
              placeholder="이메일"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
          />
          <input
              type="text"
              placeholder="사용자명"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
          />
          <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
          />
          <input type="file" accept="image/*" onChange={handleFileChange} />
          <button type="submit">가입하기</button>
        </form>
        <p style={{ marginTop: '10px' }}>
          이미 계정이 있나요?{' '}
          <span
              style={{ color: '#3ba55d', cursor: 'pointer' }}
              onClick={onGoLogin}
          >
          로그인
        </span>
        </p>
      </div>
  );
}

/* 3) 채널 목록 & 채널 생성 */
function ChannelList({ currentUser, onSelectChannel }) {
  const [channels, setChannels] = useState([]);
  const [showCreate, setShowCreate] = useState(false);
  const [channelName, setChannelName] = useState('');
  const [description, setDescription] = useState('');
  const [error, setError] = useState('');

  // 채널 목록 불러오기
  const fetchChannels = async () => {
    try {
      // GET /api/channels
      const res = await axios.get(`${API_BASE_URL}/channels`);
      setChannels(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchChannels();
  }, []);

  // 채널 생성
  const handleCreateChannel = async () => {
    setError('');
    if (!channelName.trim()) {
      setError('채널 이름을 입력하세요');
      return;
    }
    try {
      // POST /api/channels
      await axios.post(`${API_BASE_URL}/channels`, {
        name: channelName,
        description,
      });
      setChannelName('');
      setDescription('');
      setShowCreate(false);
      fetchChannels(); // 재로딩
    } catch (err) {
      setError(
          err.response?.data?.message || '채널 생성에 실패했습니다. 다시 시도해주세요.'
      );
    }
  };

  return (
      <div className="sidebar">
        <h3>채널 목록</h3>
        <div className="channel-list">
          {channels.map((ch) => (
              <div
                  key={ch.id}
                  style={{ marginBottom: '8px', cursor: 'pointer' }}
                  onClick={() => onSelectChannel(ch)}
              >
                # {ch.name}
              </div>
          ))}
        </div>
        <button onClick={() => setShowCreate(!showCreate)}>채널 생성</button>
        {showCreate && (
            <div style={{ marginTop: '10px' }}>
              {error && <p style={{ color: 'red' }}>{error}</p>}
              <input
                  type="text"
                  placeholder="채널 이름"
                  value={channelName}
                  onChange={(e) => setChannelName(e.target.value)}
              />
              <input
                  type="text"
                  placeholder="채널 설명"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
              />
              <button onClick={handleCreateChannel}>생성</button>
            </div>
        )}
        <div className="member-list" style={{ marginTop: '30px' }}>
          <p>로그인 사용자: {currentUser?.username}</p>
        </div>
      </div>
  );
}

/* 4) 채팅 창 */
function ChatWindow({ channel, currentUser }) {
  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState('');
  const [error, setError] = useState('');

  const fetchMessages = async () => {
    if (!channel) return;
    try {
      // GET /api/channels/{channelId}/messages
      const res = await axios.get(`${API_BASE_URL}/channels/${channel.id}/messages`);
      setMessages(res.data);
    } catch (err) {
      setError('메시지 로딩 실패');
    }
  };

  useEffect(() => {
    fetchMessages();
    // eslint-disable-next-line
  }, [channel]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!newMsg.trim()) return;
    try {
      // POST /api/channels/{channelId}/messages
      await axios.post(`${API_BASE_URL}/channels/${channel.id}/messages`, {
        userId: currentUser.id,
        content: newMsg,
      });
      setNewMsg('');
      fetchMessages();
    } catch (err) {
      setError('메시지 전송 실패');
    }
  };

  if (!channel) {
    return (
        <div className="chat-window">
          <h2>채널을 선택하세요</h2>
        </div>
    );
  }

  return (
      <div className="chat-window">
        <h2>{channel.name}</h2>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <div className="chat-messages">
          {messages.map((msg) => (
              <div key={msg.id} style={{ marginBottom: '8px' }}>
                <strong>{msg.username}</strong>: {msg.content}{' '}
                <span style={{ fontSize: '0.8em', color: 'gray' }}>
              {new Date(msg.createdAt).toLocaleTimeString()}
            </span>
              </div>
          ))}
        </div>
        <form onSubmit={sendMessage} className="chat-input">
          <input
              type="text"
              value={newMsg}
              onChange={(e) => setNewMsg(e.target.value)}
              placeholder="메시지를 입력하세요"
          />
          <button type="submit">전송</button>
        </form>
      </div>
  );
}

/* 5) 메인 App */
export default function App() {
  const [view, setView] = useState('login'); // 'login' | 'register' | 'channels'
  const [currentUser, setCurrentUser] = useState(null);
  const [selectedChannel, setSelectedChannel] = useState(null);

  // 로그인 성공 시
  const handleLoginSuccess = (userData) => {
    setCurrentUser(userData);
    setView('channels');
  };

  // 회원가입 완료 시 → 로그인 화면으로 이동
  const handleRegistered = () => {
    setView('login');
  };

  // 채널 선택 시
  const handleSelectChannel = (channel) => {
    setSelectedChannel(channel);
  };

  // 화면 전환
  if (view === 'login') {
    return (
        <LoginForm
            onLoginSuccess={handleLoginSuccess}
            onGoRegister={() => setView('register')}
        />
    );
  }

  if (view === 'register') {
    return (
        <RegistrationForm
            onRegistered={handleRegistered}
            onGoLogin={() => setView('login')}
        />
    );
  }

  // 채널 목록 & 채팅
  if (view === 'channels') {
    return (
        <div className="main-layout">
          <ChannelList currentUser={currentUser} onSelectChannel={handleSelectChannel} />
          <ChatWindow channel={selectedChannel} currentUser={currentUser} />
        </div>
    );
  }

  return null;
}
