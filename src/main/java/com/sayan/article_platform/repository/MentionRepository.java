package com.sayan.article_platform.repository;


import com.sayan.article_platform.entity.Mention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MentionRepository extends JpaRepository<Mention, UUID> {
}