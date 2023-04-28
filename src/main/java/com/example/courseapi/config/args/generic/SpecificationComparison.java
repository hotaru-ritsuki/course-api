package com.example.courseapi.config.args.generic;

import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum SpecificationComparison {

    ENDS_WITH(set("endsWith")),
    STARTS_WITH(set("startsWith")),
    IN_RANGE(set("inRange")),
    IN(set("in")),
    EQUALS(set("equals", "=")),
    CONTAINS(set("contains")),
    LESS_THAN_OR_EQUAL(set("lessThanOrEqual", "<=")),
    LESS_THAN(set("lessThan", "<")),
    GREATER_THAN_OR_EQUAL(set("greaterThanOrEqual", ">=")),
    GREATER_THAN(set("greaterThan", ">")),
    NOT_IN(set("notIn"));
    private final Set<String> aliases;

    private static Set<String> set(final String... args) {
        return Set.of(args);
    }

    public static SpecificationComparison getByAlias(final String alias) {
        return Stream.of(SpecificationComparison.values())
                .filter(s -> s.aliases.contains(alias))
                .findAny()
                .orElseThrow(() -> new SystemException("Unable to locate comparison param for alias " + alias, ErrorCode.NOT_FOUND));
    }

}