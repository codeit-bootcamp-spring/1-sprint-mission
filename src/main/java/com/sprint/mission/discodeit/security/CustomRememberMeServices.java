package com.sprint.mission.discodeit.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

    public CustomRememberMeServices(String key,
        UserDetailsService userDetailsService,
        PersistentTokenRepository tokenRepository) {

        super(key, userDetailsService, tokenRepository);

        setCookieName("remember-me");
        setTokenValiditySeconds(60 * 60 * 24 * 21);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {

        String rememberMeValue = request.getParameter("remember-me"); // 폼, 쿼리 상관없이

        if (rememberMeValue != null) {
            return rememberMeValue.equalsIgnoreCase("true");
        }

        // 기본 동작 유지
        return super.rememberMeRequested(request, parameter);
    }

}
