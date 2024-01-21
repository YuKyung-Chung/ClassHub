package com.f17coders.classhub.module.domain.communityScrap;

import com.f17coders.classhub.module.domain.BaseEntity;
import com.f17coders.classhub.module.domain.community.Community;
import com.f17coders.classhub.module.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityScrap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_scrap_id")
    private int communityScrapId;

    // CommunityScrap - Member 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    // CommunityScrap - Community 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    @Setter
    private Community community;

    public void putCommunity(Community community){   // 연관 관계 편의 메서드
        this.community = community;
        community.getCommunityScrapList().add(this);
    }

    // 생성 메서드
    public static CommunityScrap createCommunityScrap(Community community, Member member){
        CommunityScrap communityScrap = new CommunityScrap();

        communityScrap.setCommunity(community);
        communityScrap.setMember(member);

        return communityScrap;
    }
}

