package com.example.courseapi.config;

import com.example.courseapi.config.args.resolver.CustomDefaultFiltersArgumentResolver;
import com.example.courseapi.config.args.resolver.DefaultFiltersArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(filtersHandlerMethodArgumentResolver());
    }

    @Bean
    public DefaultFiltersArgumentResolver filtersHandlerMethodArgumentResolver() {
        return new CustomDefaultFiltersArgumentResolver("filter");
    }
}
