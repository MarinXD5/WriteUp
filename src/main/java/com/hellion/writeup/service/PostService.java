package com.hellion.writeup.service;

import com.hellion.writeup.models.Account;
import com.hellion.writeup.models.Post;
import com.hellion.writeup.repository.AccountRepository;
import com.hellion.writeup.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public String getPost(Long id, Model model){
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post";
        } else {
            return "404";
        }
    }

    public String updatePost(Long id, Post post, BindingResult result, Model model){
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            postRepository.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

    //GET
    public String createNewPost(Model model, Principal principal){

        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Account optionalAccount = accountRepository.findOneByEmailIgnoreCase(authUsername);
        if (optionalAccount != null) {
            Post post = new Post();
            post.setAccount(optionalAccount);
            post.setCreatedAt(LocalDateTime.now());
            model.addAttribute("post", post);
            return "post_new";
        } else {
            return "redirect:/";
        }
    }

    //POST
    public String createNewPost(Post post, Principal principal){
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        /*
        if (post.getAccount().getEmail().compareToIgnoreCase(authUsername) < 0) {
            ERROR: ACCOUNT MAIL != ACCOUNT USERNAME
        }*/

        postRepository.save(post);
        return "redirect:/posts/" + post.getId();
    }

    public String getPostForEdit(Long id, Model model){
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }

    public String deletePost(Long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            postRepository.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }
    }

    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    public List<Post> searchPost(String keyword){
        return postRepository.findByTitle(keyword);
    }
}
