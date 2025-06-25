**상황**

3000:80 으로 Nginx 서버를 연결해두고, Nginx 서버를 리버스 프록시 서버로서 등록해뒀지만 백엔드 서버와 연결이 되지 않는 문제가 발생했다.

**문제가 일어난 이유 : default.conf 문법 오류 | $uri $uri을 url로 작성**

```yaml
services:
  # reverse proxy
  reverse-proxy:
    container_name: reverse-proxy
    image: nginx:1.28-alpine # 250423 stable
    ports:
      - "3000:80"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/default.conf:/etc/nginx/conf.d/default.conf:ro
      - frontend_build:/usr/share/nginx/html
```

default.conf 작성 후 컨테이너의 저장소에 옮기는 작업을 하지 않아,`./nginx/default.conf:/etc/nginx/conf.d/default.conf:ro`
해당 부분을 추가했다.

```yaml

# 통신할 백엔드 서버 그룹 정의
  upstream backend_servers{
  server backend:8080;
}

  # 실제 웹 서버의 동작을 정의

  server{
  listen 80;

  # 1. API 요청 처리 (/api/*)
  location /api/ {
  proxy_pass http://backend_servers;
  proxy_set_header Host $host;
  proxy_set_header X-Real-IP $remote_addr;
  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header X-Forwarded-Proto $scheme;
  }

  # 2. 웹소켓 요청 처리 (/ws/*)
  location /ws/ {
  proxy_pass http://backend_servers;
  proxy_http_version 1.1;
  proxy_set_header Upgrade $http_upgrade;
  proxy_set_header Connection "Upgrade";
  proxy_set_header Host $host;
  }

  # 3. 그 외 모든 요청 처리 (프론트엔드)
  location / {
  root /usr/share/nginx/html;
  index index.html;
  try_files $url $url/ /index.html; # 해당 부분
  }
}
```

**문제 해결 : uri → url**

```yaml

# 통신할 백엔드 서버 그룹 정의
  upstream backend_servers{
  server backend:8080;
}

  # 실제 웹 서버의 동작을 정의

  server{
  listen 80;

  # 1. API 요청 처리 (/api/*)
  location /api/ {
  proxy_pass http://backend_servers;
  proxy_set_header Host $host;
  proxy_set_header X-Real-IP $remote_addr;
  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header X-Forwarded-Proto $scheme;
  }

  # 2. 웹소켓 요청 처리 (/ws/*)
  location /ws/ {
  proxy_pass http://backend_servers;
  proxy_http_version 1.1;
  proxy_set_header Upgrade $http_upgrade;
  proxy_set_header Connection "Upgrade";
  proxy_set_header Host $host;
  }

  # 3. 그 외 모든 요청 처리 (프론트엔드)
  location / {
  root /usr/share/nginx/html;
  index index.html;
  try_files $uri $uri/ /index.html; # 변경 완료
  }
}
```