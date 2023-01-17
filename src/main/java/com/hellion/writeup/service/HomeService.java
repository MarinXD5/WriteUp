package com.hellion.writeup.service;

import com.hellion.writeup.models.Post;
import com.hellion.writeup.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class HomeService {

    @Autowired
    private PostRepository postRepository;

    public String home(Model model){
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }
}
