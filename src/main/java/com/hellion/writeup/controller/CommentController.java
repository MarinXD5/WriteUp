package com.hellion.writeup.controller;

import com.hellion.writeup.models.Comment;
import com.hellion.writeup.models.Post;
import com.hellion.writeup.repository.CommentRepository;
import com.hellion.writeup.repository.PostRepository;
import com.hellion.writeup.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommentController{

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/post/{postId}")
    public String post(@PathVariable Long postId, Model model) {
        Post post = postRepository.findById(postId).get();
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping("/posts/{postId}/comment")
    public String addComment(@PathVariable("postId") Long postId, @RequestParam String commentText, Model model){
        commentService.addComment(postId, commentText);
        Post post = postRepository.findById(postId).get();
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping("/comment/{commentId}/reply")
    public String addReply(@PathVariable("commentId")Long commentId, @RequestParam String replyText, Model model){
        commentService.addReply(commentId, replyText);
        Comment comment = commentRepository.findById(commentId).get();
        model.addAttribute("post", comment.getPost());
        return "post";
    }

    @GetMapping("/getComments")
    public List<Comment> getComments(){
        return commentService.getComments();
    }

}
