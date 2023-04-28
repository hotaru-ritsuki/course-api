package com.example.courseapi.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class TestUtil {

    private TestUtil() {
        throw new IllegalStateException("Can not create instance of utility class");
    }

    public static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        SimpleFilterProvider sfp = new SimpleFilterProvider();
        sfp.setFailOnUnknownId(false);
        mapper.setFilterProvider(sfp);
        return mapper;
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return MAPPER.writeValueAsBytes(object);
    }

    public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
        T domainObject1 = clazz.getConstructor().newInstance();
        assertNotNull(domainObject1.toString());
        assertEquals(domainObject1, domainObject1);
        assertEquals(domainObject1.hashCode(), domainObject1.hashCode());
        // Test with an instance of another class
        Object testOtherObject = new Object();
        assertNotEquals(domainObject1, testOtherObject);
        assertNotNull(domainObject1);
        // Test with an instance of the same class
        T domainObject2 = clazz.getConstructor().newInstance();
        assertEquals(domainObject1, domainObject2);
        // HashCodes are equals because the objects are not persisted yet
        assertEquals(domainObject1.hashCode(), domainObject2.hashCode());
    }

    /**
     * Makes a executes a query to the EntityManager finding stored object for given entity class.
     *
     * @param <T>  The type of objects to be searched
     * @param em   The instance of the EntityManager
     * @param clazz The class type to be searched
     * @return A list of all found objects
     */
    public static <T> List<T> findOne(EntityManager em, Class<T> clazz) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all).setMaxResults(1);
        return allQuery.getResultList();
    }
}
