package com.example.courseapi.util;

import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Utility class to help serialize/deserialize objects
 */
@Log4j2
public class JacksonUtil {
    public static final String JSON_PROPERTIES_FILTER = "propertyFilter";

    public static final ObjectMapper objectMapperWithTimestampDateFormat;

    static {
        objectMapperWithTimestampDateFormat = new ObjectMapper();
        objectMapperWithTimestampDateFormat.registerModule(new JavaTimeModule());
        objectMapperWithTimestampDateFormat.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapperWithTimestampDateFormat.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapperWithTimestampDateFormat.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperWithTimestampDateFormat.registerModule(new SimpleModule());
    }

    private JacksonUtil() {
        throw new IllegalStateException("Can not create instance of utility class");
    }

    public static <T> T deserialize(final String json, final Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapperWithTimestampDateFormat.readValue(json, clazz);
        } catch (final IOException e) {
            throw new SystemException("Cannot deserialize", e, ErrorCode.BAD_REQUEST);
        }
    }

    public static <T> T deserialize(final String json, final TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapperWithTimestampDateFormat.readValue(json, type);
        } catch (final IOException e) {
            throw new SystemException("Cannot deserialize", e, ErrorCode.BAD_REQUEST);
        }
    }

    public String wrapper(final Object o){
        return serialize(o);
    }

    public static String serialize(final Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapperWithTimestampDateFormat.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            log.warn("Cannot serialize: ", e);
            throw new SystemException("Cannot serialize: ", e, ErrorCode.BAD_REQUEST);
        }
    }

    public static void hasAllFields(Class<?> target, String[] fields) {
        if (fields == null || fields.length == 0) return;
        List<String> missingFields = new ArrayList<>();
        for (String field : fields) {
            if (ReflectionUtils.findField(target, field) == null) {
                missingFields.add(field);
            }
        }

        if (missingFields.size() != 0) {
            String errorFields = missingFields.stream().collect(Collectors.joining(",", "[", "]"));
            throw new SystemException("Following fields don't exist: "+ errorFields, ErrorCode.BAD_REQUEST);
        }
    }

    public static MappingJacksonValue filterProperties(final Object toFilter, final String[] properties) {
        return filterProperties(toFilter, properties, toFilter.getClass());
    }

    public static MappingJacksonValue filterProperties(final Object toFilter, final String[] properties, Class<?> filterTarget) {
        hasAllFields(filterTarget, properties);
        final MappingJacksonValue jacksonValue = new MappingJacksonValue(toFilter);
        final SimpleBeanPropertyFilter filter = Objects.isNull(properties) ? SimpleBeanPropertyFilter.serializeAll()
                : SimpleBeanPropertyFilter.filterOutAllExcept(properties);

        final FilterProvider filters = new SimpleFilterProvider().addFilter(JSON_PROPERTIES_FILTER, filter);
        jacksonValue.setFilters(filters);
        return jacksonValue;
    }

}