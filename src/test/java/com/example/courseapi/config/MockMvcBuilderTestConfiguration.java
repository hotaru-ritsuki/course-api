package com.example.courseapi.config;

import com.example.courseapi.config.args.resolver.DefaultFiltersArgumentResolver;
import com.example.courseapi.controller.ExceptionTranslator;
import com.example.courseapi.security.filters.JwtAuthenticationFilter;
import com.example.courseapi.security.service.JwtService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Component
@RequiredArgsConstructor
public class MockMvcBuilderTestConfiguration {

    private final MappingJackson2HttpMessageConverter[] jacksonMessageConverters;
    private final PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    private final DefaultFiltersArgumentResolver filtersArgumentResolver;
    private final ExceptionTranslator exceptionTranslator;
    private final Validator validator;
    private final WebApplicationContext webApplicationContext;
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    public MockMvc forControllers(Object... forController) {
        return MockMvcBuilders
                .standaloneSetup(forController)
                .setCustomArgumentResolvers(pageableArgumentResolver, filtersArgumentResolver, authenticationPrincipalArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverters)
                .setValidator(validator)
                .build();
    }

    public MockMvc forSecurityControllers(Object... forController) {
        return MockMvcBuilders
                .standaloneSetup(forController)
                .apply(springSecurity(webApplicationContext.getBean( "springSecurityFilterChain", Filter.class)))
                .setCustomArgumentResolvers(pageableArgumentResolver, filtersArgumentResolver, authenticationPrincipalArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverters)
                .setValidator(validator)
                .build();
    }

    public MockMvc fromSecurityWebAppContext() {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /**
     * Create a {@link FormattingConversionService} which use ISO date format, instead of the localized one.
     *
     * @return the {@link FormattingConversionService}.
     */
    public static FormattingConversionService createFormattingConversionService() {
        DefaultFormattingConversionService dfcs = new DefaultFormattingConversionService();
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(dfcs);
        return dfcs;
    }


}
