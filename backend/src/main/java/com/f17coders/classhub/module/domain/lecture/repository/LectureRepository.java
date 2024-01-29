package com.f17coders.classhub.module.domain.lecture.repository;

import com.f17coders.classhub.module.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

	Lecture findByLectureId(int lectureId);
}
