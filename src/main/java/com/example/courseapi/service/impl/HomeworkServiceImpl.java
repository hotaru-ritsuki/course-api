package com.example.courseapi.service.impl;

import com.example.courseapi.domain.Homework;
import com.example.courseapi.dto.HomeworkDTO;
import com.example.courseapi.repository.HomeworkRepository;
import com.example.courseapi.service.HomeworkService;
import com.example.courseapi.service.mapper.HomeworkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;


    @Override
    public Optional<HomeworkDTO> findById(Long homeworkId) {
        return homeworkRepository.findById(homeworkId).map(homeworkMapper::toDto);
    }

    @Override
    public HomeworkDTO save(HomeworkDTO homeworkDTO) {
        Homework homework = homeworkMapper.toEntity(homeworkDTO);
        homework = homeworkRepository.save(homework);

        return homeworkMapper.toDto(homework);
    }

    @Override
    public List<HomeworkDTO> findAll() {
        return homeworkMapper.toDto(homeworkRepository.findAll());
    }

    @Override
    public void delete(Long homeworkId) {
        homeworkRepository.deleteById(homeworkId);
    }

    @Override
    public List<HomeworkDTO> findByStudent(Long studentId) {
        return homeworkMapper.toDto(homeworkRepository.findByStudentId(studentId));
    }

    @Override
    public List<HomeworkDTO> findByLessonId(Long lessonId) {
        return homeworkMapper.toDto(homeworkRepository.findByLessonId(lessonId));
    }
}
