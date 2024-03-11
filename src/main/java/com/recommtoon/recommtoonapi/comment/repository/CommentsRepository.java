package com.recommtoon.recommtoonapi.comment.repository;

import com.recommtoon.recommtoonapi.comment.entity.Comments;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Optional<List<Comments>> findByWebtoonId(Long webtoonId);
}
