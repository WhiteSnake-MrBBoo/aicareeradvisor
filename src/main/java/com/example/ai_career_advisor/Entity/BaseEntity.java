package com.example.ai_career_advisor.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass //연결용
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    //생성일자
    @Column(name="regDate", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime regDate;

    //수정일자
    @Column(name = "modDate")
    @LastModifiedDate
    private LocalDateTime modDate;

}
