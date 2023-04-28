package com.example.courseapi.config.args.specs;

import com.example.courseapi.config.args.generic.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Log4j2
@RequiredArgsConstructor
public class SpecificationImpl<T> implements Specification<T> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private final Filter filter;

    @Override
    @SuppressWarnings("all")
    public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

        if (filter.value() == null) {
            return null; //Invalid criteria
        }

        From from;
        String key = "";
        final String filterName = filter.name();
        if (!filterName.contains(".")) {
            from = root;
            key = filterName;
        } else if (filterName.indexOf(".") > 0) {
            final String[] propertyWithJoinedTables = filterName.split("\\.");
            from = root;
            for (int i = 0; i < propertyWithJoinedTables.length; i++) {
                if (i != propertyWithJoinedTables.length - 1) {
                    from = from.join(propertyWithJoinedTables[i]);
                } else {
                    key = propertyWithJoinedTables[i];
                }
            }
        } else {
            return null;
        }

        try {
            final Path path = from.get(key);
            switch (filter.comparison()) {
                case GREATER_THAN:
                    if (path.getJavaType() == LocalDate.class) {
                        return builder.greaterThan(path.as(LocalDate.class), getLocalDateParam());
                    }
                    if (path.getJavaType() == LocalDateTime.class) {
                        return builder.greaterThan(path.as(LocalDateTime.class), parseDateTimeSafe(true));
                    }
                    return builder.greaterThan(from.<String>get(key), getStringParam());
                case GREATER_THAN_OR_EQUAL:
                    if (path.getJavaType() == LocalDate.class) {
                        return builder.greaterThanOrEqualTo(path.as(LocalDate.class), getLocalDateParam());
                    }
                    if (path.getJavaType() == LocalDateTime.class) {
                        return builder.greaterThanOrEqualTo(path.as(LocalDateTime.class), parseDateTimeSafe(true));
                    }
                    return builder.greaterThanOrEqualTo(from.<String>get(key), getStringParam());
                case LESS_THAN:
                    if (path.getJavaType() == LocalDate.class) {
                        return builder.lessThan(path.as(LocalDate.class), getLocalDateParam());
                    }
                    if (path.getJavaType() == LocalDateTime.class) {
                        return builder.lessThan(path.as(LocalDateTime.class), parseDateTimeSafe(false));
                    }
                    return builder.lessThan(from.<String>get(key), getStringParam());
                case LESS_THAN_OR_EQUAL:
                    if (path.getJavaType() == LocalDate.class) {
                        return builder.lessThanOrEqualTo(path.as(LocalDate.class), getLocalDateParam());
                    }
                    if (path.getJavaType() == LocalDateTime.class) {
                        return builder.lessThanOrEqualTo(path.as(LocalDateTime.class), parseDateTimeSafe(false));
                    }
                    return builder.lessThanOrEqualTo(from.<String>get(key), getStringParam());
                case CONTAINS: //ignore case
                    return builder.like(builder.lower(from.<String>get(key)), "%" + getLowerCaseParam() + "%");
                case EQUALS:
                    if (path.getJavaType() == String.class) {
                        return builder.equal(from.<String>get(key), getStringParam());
                    }
                    if (path.getJavaType() == Boolean.class) {
                        return builder.equal(from.<Boolean>get(key), BooleanUtils.toBoolean(getStringParam()));
                    }
                    if (path.getJavaType() == LocalDate.class) {
                        return builder.equal(path.as(LocalDate.class), getLocalDateParam());
                    }
                    if (path.getJavaType() == LocalDateTime.class) {
                        return builder.equal(path.as(LocalDateTime.class), parseDateTime(getStringParam()));
                    }
                    if (path.getJavaType().isEnum()) {
                        return builder.equal(path, Enum.valueOf(path.getJavaType(), getStringParam()));
                    }
                    return builder.equal(path, filter.value());
                case IN:
                    if (filter.value() instanceof List) {
                        return builder.in(from.<String>get(key)).value(filter.value()); //filter.getValue() has to be a List
                    } else if (path.getJavaType() == Long.class) {
                        final List<Long> list = Arrays.stream(getDateParams()).map(Long::valueOf).collect(toList());
                        return builder.in(from.<String>get(key)).value(list);
                    } else if (path.getJavaType() == Integer.class) {
                        final List<Integer> list = Arrays.stream(getDateParams()).map(Integer::valueOf).collect(toList());
                        return builder.in(from.<String>get(key)).value(list);
                    } else if (path.getJavaType().isEnum()) {
                        final Class c = path.getJavaType();
                        return builder.in(from.<String>get(key)).value(Arrays.stream(getDateParams()).map(t -> Enum.valueOf(c, t)).collect(toList()));
                    }
                case NOT_IN:
                    if (filter.value() instanceof List) {
                        return builder.not(builder.in(from.<String>get(key)).value(filter.value())); //filter.getValue() has to be a List
                    } else if (path.getJavaType() == Long.class) {
                        List<Long> list = Arrays.stream(getValueAsStringArray()).map(Long::valueOf).collect(toList());
                        return builder.not(builder.in(from.<Long>get(key)).value(list));
                    } else if (path.getJavaType() == Integer.class) {
                        List<Integer> list = Arrays.stream(getValueAsStringArray()).map(Integer::valueOf).collect(toList());
                        return builder.not(builder.in(from.<Integer>get(key)).value(list));
                    } else if (path.getJavaType() == BigDecimal.class) {
                        List<BigDecimal> list = Arrays.stream(getValueAsStringArray()).map(BigDecimal::new).collect(toList());
                        return builder.not(builder.in(from.<BigDecimal>get(key)).value(list));
                    } else if (path.getJavaType() == Double.class) {
                        List<Double> list = Arrays.stream(getValueAsStringArray()).map(Double::parseDouble).collect(toList());
                        return builder.not(builder.in(from.<Double>get(key)).value(list));
                    } else if (path.getJavaType() == Float.class) {
                        List<Float> list = Arrays.stream(getValueAsStringArray()).map(Float::parseFloat).collect(toList());
                        return builder.not(builder.in(from.<Float>get(key)).value(list));
                    } else if (path.getJavaType().isEnum()) {
                        final Class c = path.getJavaType();
                        return builder.not(builder.in(from.<String>get(key)).value(Arrays.stream(getValueAsStringArray()).map(t -> Enum.valueOf(c, t)).collect(toList())));
                    } else if (path.getJavaType() == String.class) {
                        return builder.not(builder.in(from.<String>get(key)).value(Arrays.asList(getValueAsStringArray())));
                    }
                case IN_RANGE:

                    if (path.getJavaType() == LocalDate.class) {
                        final LocalDate fromDate = parseDateTimeWithDefaultSafe(getDateString(0), LocalDate.MIN);
                        final LocalDate toDate = parseDateTimeWithDefaultSafe(getDateString(1), LocalDate.MAX);
                        return builder.between(path.as(LocalDate.class), fromDate, toDate);
                    }
                    if (path.getJavaType() == LocalDateTime.class) {
                        final LocalDateTime fromDate = parseDateTimeWithDefaultSafe(getDateString(0), LocalDateTime.MIN, true);
                        final LocalDateTime toDate = parseDateTimeWithDefaultSafe(getDateString(1), LocalDateTime.MAX, false);
                        return builder.between(path.as(LocalDateTime.class), fromDate, toDate);
                    }

                    return builder.between(path, Double.parseDouble(getStringParam().split("-")[0]), Double.parseDouble(getStringParam().split("-")[1]));
                case STARTS_WITH: //ignore case
                    return builder.like(builder.lower(from.<String>get(key)), getLowerCaseParam() + "%");
                case ENDS_WITH: //ignore case
                    return builder.like(builder.lower(from.<String>get(key)), "%" + getLowerCaseParam());
                default:
                    return null;
            }
        } catch (final NumberFormatException e) {
            log.error("Number cannot be parsed, so number inRanage filter is ignored");
            return null;
        }
    }

    private String[] getValueAsStringArray() {
        return "".equals(getStringParam()) ? new String[]{} : getStringParam().split(",");
    }

    private String getStringParam() {
        return filter.value().toString();
    }

    private String getLowerCaseParam() {
        return getStringParam().toLowerCase();
    }

    private String getDateString(final Integer index) {
        return getDateParams()[index];
    }

    private String[] getDateParams() {
        return getStringParam().split(",");
    }

    /**
     * This method will try to parse a {@link LocalDateTime} instance from current filter string value.
     * if {@link DateTimeParseException} occurs, it will try to instantiate a
     * {@link LocalDate} instance and then apply atStartOfDay() or  date.atTime(LocalTime.MAX) based
     * on if it is a start or end search field
     *
     * @return parsed LocalDateTime
     */
    private LocalDateTime parseDateTimeSafe(final boolean isFromField) {
        return parseDateTimeSafe(getStringParam(), isFromField);
    }

    private LocalDate getLocalDateParam() {
        return LocalDate.from(DATE_FORMATTER.parse(getStringParam()));
    }

    private static LocalDate parseDate(final String dateTime) {
        return LocalDate.from(DATE_FORMATTER.parse(dateTime));
    }

    private static LocalDate parseDateTimeWithDefaultSafe(final String date, final LocalDate defDate) {
        if (StringUtils.isBlank(date) || Objects.equals("null", date)) {
            return defDate;
        }
        return LocalDate.from(DATE_FORMATTER.parse(date));
    }

    private static LocalDateTime parseDateTimeWithDefaultSafe(final String date, final LocalDateTime defDate, final boolean isFromField) {
        if (StringUtils.isBlank(date) || Objects.equals("null", date)) {
            return defDate;
        }
        return parseDateTimeSafe(date, isFromField);
    }

    private static LocalDateTime parseDateTime(final String dateTime) {
        return LocalDateTime.from(DATE_TIME_FORMATTER.parse(dateTime));
    }

    /**
     * This method will try to parse a {@link LocalDateTime} instance from string.
     * if {@link DateTimeParseException} occurs, it will try to instantiate a
     * {@link LocalDate} instance and then apply atStartOfDay() or date.atTime(LocalTime.MAX) based
     * on if it is a start or end search field
     *
     * @param dateTime date time input
     * @return parsed {@link LocalDateTime}
     */
    private static LocalDateTime parseDateTimeSafe(final String dateTime, final boolean isFromField) {
        try {
            return LocalDateTime.from(DATE_TIME_FORMATTER.parse(dateTime));
        } catch (final DateTimeParseException e) {
            final LocalDate localDate = parseDate(dateTime);
            return isFromField ? localDate.atStartOfDay() : localDate.atTime(LocalTime.MAX);
        }
    }
}
