package com.example.suemember.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor).addPathPatterns("/member/test");
        registry.addInterceptor(jwtTokenInterceptor).addPathPatterns("/members/{id}");
        registry.addInterceptor(jwtTokenInterceptor).addPathPatterns("/logout/{id}");
    }
}
