package com.example.reigie.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 自定义filter会导致继承WebMvcConfigurer中的跨域允许方法失效，因为其跨域设置在目标方法执行时进行设置，
 * filter > interceptor > 跨域配置，因此如果使用filter那么就需要注入一个优先级更高的CorsFilter进行处理跨域，同理使用拦截器，需要使用filter进行跨域处理
 */
@Configuration
public class MyCorsFilter{
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1 设置访问源地址
        corsConfiguration.addAllowedOriginPattern("*");
        // 2 设置访问源请求头
        corsConfiguration.addAllowedHeader("*");
        // 3 设置访问源请求方法
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        // 4 暴露哪些头部信息
        corsConfiguration.addExposedHeader("Access-Control-Allow-Origin");
        return corsConfiguration;
    }
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 5 对接口配置跨域设置
        source.registerCorsConfiguration("/**", buildConfig());
        // 有多个filter时此处设置改CorsFilter的优先执行顺序
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}