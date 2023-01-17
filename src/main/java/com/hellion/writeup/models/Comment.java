package com.hellion.writeup.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="COMMENT")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String author;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    public Comment(String text, Post post) {
        this.text = text;
        this.post = post;
    }

    public Comment(String text, Post post, Comment parentComment) {
        this.text = text;
        this.post = post;
        this.parentComment = parentComment;
    }



}
