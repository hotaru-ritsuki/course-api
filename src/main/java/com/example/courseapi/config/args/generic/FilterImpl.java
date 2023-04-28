package com.example.courseapi.config.args.generic;

public record FilterImpl(String name, SpecificationComparison comparison, Object value) implements Filter {
    @Override
    public String toString() {
        return String.format("%s %s %s", name, comparison, value);
    }
}
