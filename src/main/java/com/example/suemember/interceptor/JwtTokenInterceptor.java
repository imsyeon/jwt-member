package com.example.suemember.interceptor;

import com.example.suemember.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
         log.info("intercepter");
        log.info("JwtToken 호출");
        String accessToken = request.getHeader("ACCESS_TOKEN");
        log.info("AccessToken:" + accessToken);
        String refreshToken = request.getHeader("REFRESH_TOKEN");
        log.info("RefreshToken:" + refreshToken);

        if (accessToken != null && jwtTokenProvider.isValidAccessToken(accessToken)) {
            return true;
        }

        response.setStatus(401);
        response.setHeader("ACCESS_TOKEN", accessToken);
        response.setHeader("REFRESH_TOKEN", refreshToken);
        response.setHeader("msg", "Check the tokens.");
        return false;
    }
}
