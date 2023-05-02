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
 * Utility class for serialization and deserialization of JSON objects.
 */
@Log4j2
public final class JacksonUtil {

    /**
     * Constant for the filter name used when filtering properties of JSON objects.
     */
    public static final String JSON_PROPERTIES_FILTER = "propertyFilter";

    /**
     * Object mapper instance configured to handle date/time values
     */
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

    /**
     * Deserialize a JSON string into an object of a given class.
     *
     * @param json  the JSON string to deserialize.
     * @param clazz the class to deserialize into.
     * @param <T>   the type of the object to deserialize into.
     * @return the deserialized object.
     * @throws SystemException if an error occurs during deserialization.
     */
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

    /**
     * Deserialize a JSON string into an object of a given type.
     *
     * @param json the JSON string to deserialize.
     * @param type the type of object to deserialize into.
     * @param <T>  the type of the object to deserialize into.
     * @return the deserialized object.
     * @throws SystemException if an error occurs during deserialization.
     */
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

    /**
     * Serialize an object to a JSON string.
     *
     * @param object the object to serialize.
     * @return the JSON string representing the object.
     * @throws SystemException if an error occurs during serialization.
     */
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

    /**
     * Check if a class has all the fields specified by an array of field names.
     *
     * @param target the class to check.
     * @param fields an array of field names to check for.
     * @throws SystemException if one or more of the fields are missing from the class.
     */
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
            throw new SystemException("Following fields don't exist: " + errorFields, ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * Filters out properties of an object and returns a MappingJacksonValue containing only the specified properties.
     * Uses the default filter target which is the class of the object being filtered.
     *
     * @param toFilter   the object to be filtered
     * @param properties the array of properties to be included in the filtered output
     * @return a MappingJacksonValue containing only the specified properties
     * @throws SystemException if any of the specified properties do not exist in the filter target
     */
    public static MappingJacksonValue filterProperties(final Object toFilter, final String[] properties) {
        return filterProperties(toFilter, properties, toFilter.getClass());
    }

    /**
     * Filters out properties of an object and returns a MappingJacksonValue containing only the specified properties.
     *
     * @param toFilter     the object to be filtered
     * @param properties   the array of properties to be included in the filtered output
     * @param filterTarget the class whose fields will be used to filter the object properties
     * @return a MappingJacksonValue containing only the specified properties
     * @throws SystemException if any of the specified properties do not exist in the filter target
     */
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