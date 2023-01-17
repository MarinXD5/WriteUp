package com.hellion.writeup.exceptions;

public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException(Long id){
        super("Could not find comment with ID: " + id);
    }
}
