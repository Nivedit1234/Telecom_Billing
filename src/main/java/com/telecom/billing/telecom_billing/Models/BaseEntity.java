package com.telecom.billing.telecom_billing.Models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


/**
 * BaseEntity - Common audit fields for all entities.
 *
 * This class provides:
 *  1. createdAt  – timestamp of when the record was created.
 *  2. updatedAt  – timestamp of when the record was last modified.
 *
 * Annotations used:
 *  @MappedSuperclass
 *      - This class is NOT mapped to a table.
 *      - Its fields are inherited by child entities.
 *      - Prevents repeating common audit fields across models.
 *
 *  @Getter, @Setter (Lombok)
 *      - Auto-generates getter and setter methods.
 *      - Reduces boilerplate code.
 *
 *  @CreationTimestamp
 *      - Automatically sets 'createdAt' when the entity is first saved.
 *      - Uses database time, ensures reliable audit information.
 *
 *  @UpdateTimestamp
 *      - Automatically updates 'updatedAt' on every update.
 *      - Useful for tracking modifications, logs, analytics, etc.
 *
 *  @Column(updatable = false)
 *      - Ensures 'createdAt' cannot be modified after creation.
 *
 * Purpose of this class:
 *  - Provides consistent audit fields across all database tables.
 *  - Useful for debugging, admin reporting, caching, and record tracking.
 *  - Follows DRY principle (Don't Repeat Yourself).
 */


@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
