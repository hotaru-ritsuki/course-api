package com.example.courseapi.config.args.generic;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

@NoArgsConstructor
public class FiltersImpl extends ArrayList<Filter> implements Filters {

    public FiltersImpl(final Collection<? extends Filter> c) {
        super(c);
    }

    @Override
    public Iterator<Filter> iterator() {
        return Collections.unmodifiableCollection(new ArrayList<>(this)).iterator();
    }

    @Override
    public void include(final Filter filter) {
        super.add(filter);
    }

    @Override
    public void include(final Filters filters) {
        for(final Filter f : filters) {
            include(f);
        }
    }

}