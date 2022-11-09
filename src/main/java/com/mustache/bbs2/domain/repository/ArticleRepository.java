package com.mustache.bbs2.domain.repository;

import com.mustache.bbs2.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
