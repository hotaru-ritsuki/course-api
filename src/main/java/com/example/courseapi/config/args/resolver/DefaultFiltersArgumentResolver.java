package com.example.courseapi.config.args.resolver;

import com.example.courseapi.config.args.FilterUtil;
import com.example.courseapi.config.args.generic.Filters;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Class that adds support for {@link Filters} in method params
 * for Spring {@link org.springframework.stereotype.Controller} methods.
 * It has to be registered manually in {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}
 * for each new project to allow possible custom implementation.
 */
public class DefaultFiltersArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String DEFAULT_PARAMETER_NAME = "filter";


    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return Filters.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String[] requestFilters = webRequest.getParameterValues(DEFAULT_PARAMETER_NAME);
        return FilterUtil.generateDefaultFilters(requestFilters);
    }

}