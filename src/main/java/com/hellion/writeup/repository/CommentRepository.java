package com.hellion.writeup.repository;

import com.hellion.writeup.models.Account;
import com.hellion.writeup.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByParentComment(Comment parentComment);
    List<Comment> findByParentCommentIsNull();
    List<Comment> findByParentCommentIsNullOrderByCreatedAtDesc();
}
