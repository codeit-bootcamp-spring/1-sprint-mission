#!/bin/bash

# 스크립트 디렉토리 설정
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# 도움말 함수
function show_help {
  echo "DiscoDeIt Docker 환경 실행 스크립트"
  echo ""
  echo "사용법:"
  echo "  ./run.sh [명령어]"
  echo ""
  echo "명령어:"
  echo "  build      - Docker 이미지 빌드"
  echo "  start      - Docker 컨테이너 시작"
  echo "  stop       - Docker 컨테이너 중지"
  echo "  restart    - Docker 컨테이너 재시작"
  echo "  logs       - 애플리케이션 로그 확인"
  echo "  ps         - 실행 중인 컨테이너 상태 확인"
  echo "  exec       - 애플리케이션 컨테이너에 접속"
  echo "  exec-db    - 데이터베이스 컨테이너에 접속"
  echo "  clean      - 모든 컨테이너, 볼륨 삭제 (주의: 모든 데이터가 삭제됩니다)"
  echo "  help       - 도움말 표시"
}

# 명령어 처리
case "$1" in
  build)
    echo "Docker 이미지 빌드 중..."
    docker-compose build
    ;;
  start)
    echo "Docker 컨테이너 시작 중..."
    docker-compose up -d
    echo "애플리케이션은 http://localhost:80 에서 접근 가능합니다."
    ;;
  stop)
    echo "Docker 컨테이너 중지 중..."
    docker-compose down
    ;;
  restart)
    echo "Docker 컨테이너 재시작 중..."
    docker-compose down
    docker-compose up -d
    echo "애플리케이션은 http://localhost:80 에서 접근 가능합니다."
    ;;
  logs)
    echo "애플리케이션 로그 확인 중..."
    docker-compose logs -f app
    ;;
  ps)
    echo "실행 중인 컨테이너 상태:"
    docker-compose ps
    ;;
  exec)
    echo "애플리케이션 컨테이너에 접속 중..."
    docker-compose exec app sh
    ;;
  exec-db)
    echo "데이터베이스 컨테이너에 접속 중..."
    docker-compose exec db psql -U postgres -d discodeit
    ;;
  clean)
    echo "주의: 모든 컨테이너와 볼륨을 삭제합니다. 모든 데이터가 삭제됩니다."
    echo "계속하시겠습니까? (y/n)"
    read -r confirm
    if [ "$confirm" = "y" ] || [ "$confirm" = "Y" ]; then
      docker-compose down -v
      echo "모든 컨테이너와 볼륨이 삭제되었습니다."
    else
      echo "작업이 취소되었습니다."
    fi
    ;;
  help|*)
    show_help
    ;;
esac 