package com.example.courseapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonsUpdateDTO {

    @NotNull
    @Size(min = 5)
    private Set<LessonRequestDTO> lessons;
}
