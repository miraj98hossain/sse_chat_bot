package com.example.sse_chat_bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Base entity class with auditing fields
 * This class provides common fields for all entities
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * When the entity was created
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * User who created the entity
     * This is set manually in service layer
     */
    @Column(name = "created_by")
    private UUID createdBy;

    /**
     * When the entity was last updated
     */
    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    private OffsetDateTime updatedAt;

    /**
     * User who last updated the entity
     * This is set manually in service layer
     */
    @Column(name = "updated_by")
    private UUID updatedBy;

    /**
     * Version for optimistic locking
     */
    @Version
    @Column(name = "version")
    private Integer version = 0;
}
