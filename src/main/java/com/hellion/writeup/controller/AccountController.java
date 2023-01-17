package com.hellion.writeup.controller;

import com.hellion.writeup.models.Account;
import com.hellion.writeup.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/users/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/friends/add")
    public ResponseEntity addFriend(@RequestBody Map<String, List<String>> request){
        Map<String, Object> responseData = new HashMap<>();

        if(request == null || request.get("friends") == null || request.get("friends").size() != 2) {
            responseData.put("message", "Invalid request");
            return ResponseEntity.badRequest().body(responseData);
        }

        try {
            List<String> friends = request.get("friends");
            accountService.addFriend(friends.get(0), friends.get(1));
            responseData.put("success", true);
            return ResponseEntity.ok(responseData);
        }
        catch(Exception e) {
            responseData.put("success", false);
            responseData.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }
}
