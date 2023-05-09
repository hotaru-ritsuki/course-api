package com.example.courseapi.domain;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class for Lesson
 */
@Entity
@Table(name = "lessons", schema = "course_management")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Lesson extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7124353463291638392L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "title")
    private String title;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Submission> submissions = new HashSet<>();
}
