package com.example.courseapi.config.args.generic;

import java.io.Serializable;

public interface Filter extends Serializable {
    String name();
    SpecificationComparison comparison();
    Object value();
}