package com.sprint.mission.discodeit.context;

// 테넌트 컨텍스트 홀더
public class TenantContext {

  // InheritableThreadLocal > 자식 스레드 생성 시 부모의 값을 자동으로 상속
  // TaskDecorator에서 명시적으로 전파하는 편이 더 안전
  private static final ThreadLocal<String> currentTenant =
      new InheritableThreadLocal<>();

  public static void setCurrentTenant(String tenantId) {
    currentTenant.set(tenantId);
  }

  public static String getCurrentTenant() {
    return currentTenant.get();
  }

  // 메모리 누수 방지를 위해 반드시 호출
  public static void clear() {
    currentTenant.remove();
  }
}
