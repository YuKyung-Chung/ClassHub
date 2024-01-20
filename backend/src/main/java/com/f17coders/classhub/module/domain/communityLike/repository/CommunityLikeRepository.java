package com.f17coders.classhub.module.domain.communityLike.repository;

import com.f17coders.classhub.module.domain.communityLike.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Integer> {
    int countCommunityLikeByCommunity_CommunityId(int communityId);
}
