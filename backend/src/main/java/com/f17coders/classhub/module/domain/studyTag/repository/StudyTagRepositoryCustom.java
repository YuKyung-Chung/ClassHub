package com.f17coders.classhub.module.domain.studyTag.repository;

import com.f17coders.classhub.module.domain.tag.dto.response.TagRes;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface StudyTagRepositoryCustom {

    @Transactional
    void deleteStudyTagsByStudyId(int studyId);
}
