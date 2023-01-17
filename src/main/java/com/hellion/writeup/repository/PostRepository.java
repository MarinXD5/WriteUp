package com.hellion.writeup.repository;

import com.hellion.writeup.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT post FROM Post post WHERE post.title LIKE %?1%")
    List<Post> findByTitle(String keyword);
}

