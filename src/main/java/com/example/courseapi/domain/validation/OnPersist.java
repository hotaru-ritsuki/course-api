package com.example.courseapi.domain.validation;

/**
 * Bean Validation group for entity constraints that are checked only when an entity is first persisted.
 * <p>
 * Hibernate 7 also validates the owning entity when only one of its collections changes (pre-update collection
 * event). Constraints in this group keep the Hibernate 6 behaviour, where such collection-only updates
 * (e.g. removing a deleted instructor from a course) were not validated.
 * The group is enabled for pre-persist via {@code jakarta.persistence.validation.group.pre-persist} in application.yml.
 */
public interface OnPersist {
}
