package com.hellion.writeup.service;

import com.hellion.writeup.models.Comment;
import com.hellion.writeup.models.Post;
import com.hellion.writeup.repository.CommentRepository;
import com.hellion.writeup.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public void addComment(Long postId, String commentText){
        Post post = postRepository.findById(postId).get();
        Comment comment = new Comment(commentText, post);

        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public void addReply(Long commentId, String replyText){
        Comment comment = commentRepository.findById(commentId).get();
        Comment reply = new Comment(replyText, comment.getPost(), comment);
        reply.setCreatedAt(LocalDateTime.now());
        commentRepository.save(reply);
    }

    public List<Comment> getComments(){
        List<Comment> comments = commentRepository.findAll();

        return comments;
    }


}
