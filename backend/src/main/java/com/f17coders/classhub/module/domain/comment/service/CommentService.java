package com.f17coders.classhub.module.domain.comment.service;

import com.f17coders.classhub.global.exception.BaseExceptionHandler;
import com.f17coders.classhub.module.domain.comment.dto.request.CommentRegisterReq;
import com.f17coders.classhub.module.domain.comment.dto.request.CommentUpdateReq;
import com.f17coders.classhub.module.domain.member.Member;

import java.io.IOException;

public interface CommentService {
    int registerComment(int communityId, CommentRegisterReq commentRegisterReq, Member member) throws BaseExceptionHandler, IOException;

    void updateComment(int commentId, CommentUpdateReq commentUpdateReq, Member member);

    void deleteComment(int commentId, Member member);
}
