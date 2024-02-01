package com.f17coders.classhub.module.domain.lecture.repository;

import static com.f17coders.classhub.module.domain.category.QCategory.category;
import static com.f17coders.classhub.module.domain.lecture.QLecture.lecture;
import static com.f17coders.classhub.module.domain.lectureLike.QLectureLike.lectureLike;
import static com.f17coders.classhub.module.domain.lectureTag.QLectureTag.lectureTag;

import com.f17coders.classhub.module.domain.category.dto.resource.CategoryRes;
import com.f17coders.classhub.module.domain.lecture.Level;
import com.f17coders.classhub.module.domain.lecture.SiteType;
import com.f17coders.classhub.module.domain.lecture.dto.response.LectureListDetailLectureLikeCountRes;
import com.f17coders.classhub.module.domain.lecture.dto.response.LectureReadLectureLikeCountRes;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class LectureRepositoryImpl implements LectureRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public LectureRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public int countLectureBySearchCond(Integer categoryId, String tags,
		String keyword, String level, String site) {
		return Math.toIntExact(queryFactory
			.select(lecture.count())
			.from(lecture)
			.where(searchCond(categoryId, tags, keyword, level, site))
			.fetchFirst());
	}

	// select안의 Projections 중복사용된다. 리팩토링 필요
	@Override
	public LectureReadLectureLikeCountRes findLectureByLectureId(Integer lectureId) {
		return queryFactory.select(
				Projections.constructor(LectureReadLectureLikeCountRes.class,
					lecture.lectureId,
					lecture.name,
					lecture.instructor,
					Expressions.stringTemplate("COALESCE({0}, 'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791187395027.jpg')", lecture.image).as("image"),
					lecture.level,
					lecture.siteType,
					lecture.siteLink,
					lecture.priceOriginal,
					lecture.priceSale,
					lecture.totalTime,
					lecture.curriculum,
					Projections.constructor(CategoryRes.class,
						category.categoryId,
						category.categoryName
					),
					Expressions.numberTemplate(Integer.class, "{0}",
						JPAExpressions
							.select(lectureLike.lecture.lectureId.count())
							.from(lectureLike)
							.where(lecture.lectureId.eq(lectureLike.lecture.lectureId))
							.groupBy(lecture.lectureId)).as("lectureLikeCount"),
					Expressions.numberTemplate(Float.class,
						"CASE WHEN {1}=0 AND {3}=0 THEN 0 "
							+
							"WHEN {1} < 10 THEN ({0} * 0.2 + {2} * {3} * 0.8) / ({1} * 0.2 + {3} * 0.8) "
							+
							"ELSE ({0} * 0.5 + {2} * {3} * 0.5) / ({1} * 0.5 + {3} * 0.5) END",
						lecture.reviewSum, lecture.reviewCount, lecture.siteReviewRating,
						lecture.siteReviewCount).as("combinedRating"),
					Expressions.numberTemplate(Integer.class, "{0} + {1}", lecture.reviewCount,
						lecture.siteReviewCount).as("combinedRatingCount"),
					Expressions.numberTemplate(Float.class,
						"CASE WHEN {1}=0 OR {0}=0 THEN 0 "
							+
							"ELSE {0} / {1} END", lecture.reviewSum,
						lecture.reviewCount).as("reviewRating"),
					lecture.reviewCount,
					lecture.siteReviewRating,
					lecture.siteReviewCount,
					lecture.siteStudentCount,
					lecture.gptReviewGood,
					lecture.gptReviewBad,
					lecture.descriptionSummary,
					lecture.summary,
					lecture.descriptionDetail
				))
			.from(lecture)
			.where(lecture.lectureId.eq(lectureId))
			.fetchOne();
	}

	public List<LectureListDetailLectureLikeCountRes> findLecturesBySearchCond(Integer categoryId,
		String tags, String keyword, String level, String site, String order, Pageable pageable) {
		List<LectureListDetailLectureLikeCountRes> lectureListDetailLectureLikeCountRes = queryFactory.select(
				Projections.constructor(LectureListDetailLectureLikeCountRes.class,
					lecture.lectureId,
					lecture.name,
					lecture.siteType,
					lecture.instructor,
					Expressions.stringTemplate("COALESCE({0}, 'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791187395027.jpg')", lecture.image).as("image"),
					lecture.level,
					Expressions.numberTemplate(Float.class,
						"CASE WHEN {1}=0 AND {3}=0 THEN 0 "
							+
							"WHEN {1} < 10 THEN ({0} * 0.2 + {2} * {3} * 0.8) / ({1} * 0.2 + {3} * 0.8) "
							+
							"ELSE ({0} * 0.5 + {2} * {3} * 0.5) / ({1} * 0.5 + {3} * 0.5) END",
						lecture.reviewSum, lecture.reviewCount, lecture.siteReviewRating,
						lecture.siteReviewCount).as("combinedRating"),
					Expressions.numberTemplate(Integer.class, "{0} + {1}", lecture.reviewCount,
						lecture.siteReviewCount).as("combinedRatingCount"),
					lecture.priceOriginal,
					lecture.priceSale,
					lecture.descriptionSummary,
					lecture.totalTime,
					Projections.constructor(CategoryRes.class,
						category.categoryId,
						category.categoryName
					),
					Expressions.numberTemplate(Integer.class, "{0}",
						JPAExpressions
							.select(lectureLike.lecture.lectureId.count())
							.from(lectureLike)
							.where(lecture.lectureId.eq(lectureLike.lecture.lectureId))
							.groupBy(lecture.lectureId)).as("lectureLikeCount")
				))
			.from(lecture)
			.where(searchCond(categoryId, tags, keyword, level, site))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

//		for (LectureListDetailLectureLikeCountRes ll : lectureListDetailLectureLikeCountRes) {
//			System.out.println(ll);
//			break;
//		}

		return lectureListDetailLectureLikeCountRes;


	}

	private BooleanExpression categoryIdEq(Integer categoryId) {
		return categoryId!=null ? lecture.category.categoryId.eq(categoryId)
			: null;
	}

	private BooleanExpression keywordLike(String keyword) {
		return StringUtils.hasText(keyword) ? lecture.name.contains(keyword)
			.or(lecture.instructor.contains(keyword)) : null;
	}

	private BooleanExpression levelEq(String level) {
		return StringUtils.hasText(level) ? lecture.level.eq(Level.valueOf(level)) : null;
	}

	private BooleanExpression siteEq(String site) {
		return StringUtils.hasText(site) ? lecture.siteType.eq(SiteType.valueOf(site)) : null;
	}

	private BooleanExpression tagsCond(String tags) {
		if (StringUtils.hasText(tags)) {
			// 쉼표(,)를 기준으로 문자열을 분할하여 배열로 얻습니다.
			String[] tagsArray = tags.split("\\|\\|");
			System.out.println(Arrays.deepToString(tagsArray));
			// 배열을 int 타입의 리스트로 변환합니다.
			List<Integer> tagsList = Arrays.stream(tagsArray)
				.map(Integer::parseInt)
				.collect(Collectors.toList());

			return lecture.lectureId.in(
				JPAExpressions
					.select(lectureTag.lecture.lectureId)
					.from(lectureTag)
					.where(lectureTag.tag.tagId.in(tagsList)));
		} else {
			return null;
		}
	}

	private BooleanBuilder searchCond(Integer categoryId, String tags, String keyword, String level,
		String site) {
		BooleanBuilder builder = new BooleanBuilder();

		return builder
			.and(tagsCond(tags))
			.and(siteEq(site))
			.and(levelEq(level))
			.and(categoryIdEq(categoryId))
			.and(keywordLike(keyword));
	}

}
