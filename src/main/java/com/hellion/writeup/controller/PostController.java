package com.hellion.writeup.controller;

import com.hellion.writeup.models.Post;
import com.hellion.writeup.models.SearchForm;
import com.hellion.writeup.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        return postService.getPost(id, model);
    }

    @PostMapping("/posts/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updatePost(@PathVariable Long id, Post post, BindingResult result, Model model) {
        return postService.updatePost(id, post, result, model);
    }

    @GetMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(Model model, Principal principal) {
        return postService.createNewPost(model, principal);
    }

    @PostMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(@ModelAttribute Post post, Principal principal) {
        return postService.createNewPost(post, principal);
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getPostForEdit(@PathVariable Long id, Model model) {
        return postService.getPostForEdit(id, model);
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @GetMapping("/search")
    public String search(SearchForm searchForm, Model model){
        List<Post> posts = postService.searchPost(searchForm.getKeyword());
        model.addAttribute("posts", posts);
        return "search-result";
    }

}
