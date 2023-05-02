package com.example.courseapi.config.args;

import com.example.courseapi.config.args.generic.*;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.stream.Stream;


public final class FilterUtil {
    public static final String DEFAULT_PROPERTY_DELIMITER = ":";
    public static final int FILTER_SPLIT_SIZE = 3;
    public static final Splitter SPLITTER = Splitter.on(DEFAULT_PROPERTY_DELIMITER).limit(FILTER_SPLIT_SIZE).trimResults().omitEmptyStrings();

    private FilterUtil() {
        throw new IllegalStateException("Can not create instance of utility class");
    }

    public static Filters generateDefaultFilters(final String[] requestFilters){
        final List<Filter> collect = FilterUtil.split(requestFilters).stream()
                .map(FilterUtil::generateFilter)
                .collect(Collectors.toList());
        return new FiltersImpl(collect);
    }

    public static Filter generateFilter(final List<String> requestFilter) {
        if (requestFilter.size() != 3){
            throw new SystemException(String.format("Filter length must be %s, but was %s", FILTER_SPLIT_SIZE, requestFilter.size()), ErrorCode.BAD_REQUEST);
        }
        final String name = requestFilter.get(0);
        final String comparison = requestFilter.get(1);
        final String value = requestFilter.get(2);
        return new FilterImpl(name, SpecificationComparison.getByAlias(comparison), value);
    }

    public static Set<List<String>> split(final String[] requestFilters) {
        return split(SPLITTER, FILTER_SPLIT_SIZE, requestFilters);
    }

    public static Set<List<String>> split(final Splitter splitter, final int filterSplitSize, final String... requestFilters) {
        return Stream.ofNullable(requestFilters)
                .flatMap(Stream::of)
                .filter(StringUtils::isNoneEmpty)
                .map(splitter::splitToList)
                .filter(filter -> filter.size() == filterSplitSize)
                .collect(Collectors.toSet());
    }
}