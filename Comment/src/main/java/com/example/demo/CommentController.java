package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @GetMapping("/comments")
    List<CommentDTO> all() {
        return commentService.getAllComments();
    }

    @PostMapping("/comments")
    void Insert(@RequestBody Comment newComment) {
        commentService.saveOrUpdate(newComment);
    }


    @GetMapping("/comments/{id}")
    Comment findComment(@PathVariable Integer id) {

        return commentService.geUserById(id);
    }


    @DeleteMapping("/comments/{id}")
    void deleteUser( @PathVariable Integer id) {
        commentService.delete(id);
    }

    @PostMapping("/updateMessage/{id}")
    void updatePassword( @RequestBody String message, @PathVariable Integer id) {

        commentService.UpdateMessage(id, message);
    }




}
