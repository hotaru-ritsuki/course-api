package com.example.courseapi.config.args.generic;

public interface Filters extends Iterable<Filter> {
    void include(Filter filer);
    void include(Filters filers);
}