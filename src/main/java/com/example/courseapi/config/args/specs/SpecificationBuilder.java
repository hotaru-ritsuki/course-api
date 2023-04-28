package com.example.courseapi.config.args.specs;

import com.example.courseapi.config.args.generic.Filter;
import com.example.courseapi.config.args.generic.Filters;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class SpecificationBuilder<T> {

    private final List<SpecificationImpl<T>> specs = new ArrayList<>();

    public SpecificationBuilder(final Filter filter) {
        this.specs.add(new SpecificationImpl<>(filter));
    }

    public SpecificationBuilder(final Filters filters) {
        if (Objects.nonNull(filters)) {
            filters.forEach(f -> this.specs.add(new SpecificationImpl<>(f)));
        }
    }

    public SpecificationBuilder<T> with(final Filter filter) {
        if (filter != null) {
            this.specs.add(new SpecificationImpl<>(filter));
        }
        return this;
    }

    public Specification<T> build() {
        if (specs.size() == 0) {
            return null;
        }
        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }


}
