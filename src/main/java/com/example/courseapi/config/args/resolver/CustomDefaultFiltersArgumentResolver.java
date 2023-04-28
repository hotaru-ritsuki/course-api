package com.example.courseapi.config.args.resolver;

import com.example.courseapi.config.args.FilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class CustomDefaultFiltersArgumentResolver extends DefaultFiltersArgumentResolver {

    private final String filterName;

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        return FilterUtil.generateDefaultFilters(webRequest.getParameterValues(filterName));
    }
}