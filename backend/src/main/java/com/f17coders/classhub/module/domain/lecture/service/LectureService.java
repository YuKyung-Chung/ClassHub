package com.f17coders.classhub.module.domain.lecture.service;

import com.f17coders.classhub.global.exception.BaseExceptionHandler;
import com.f17coders.classhub.module.domain.lecture.dto.response.LectureListDetailRes;
import com.f17coders.classhub.module.domain.lecture.dto.response.LectureListRes;
import com.f17coders.classhub.module.domain.lecture.dto.response.LectureReadRes;
import com.querydsl.core.Tuple;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface LectureService {

	LectureReadRes readLecture(int lectureId) throws BaseExceptionHandler, IOException;

	LectureListRes getLecturesList(String categoryName, String keyword, String level, String site,
		String order, Pageable pageable) throws BaseExceptionHandler, IOException;
}
