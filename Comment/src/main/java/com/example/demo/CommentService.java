package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    public List<CommentDTO> getAllComments() {
        List<CommentDTO>  comments= new ArrayList<>();
        commentRepository.findAll().forEach(comment -> comments.add(new CommentDTO(comment.getId(),comment.getMessage())));
        return comments;
    }


    //getting a specific record by using the method findById() of CrudRepository
    public Comment geUserById(int id) {
        return commentRepository.findById(id).get();
    }

    //saving a specific record by using the method save() of CrudRepository
    public void saveOrUpdate(Comment comment)
    {

        commentRepository.save(comment);
    }


    public void UpdateMessage(Integer id, String message) {
        Comment comment = geUserById(id);
        comment.setMessage(message);
        commentRepository.save(comment);
    }

    //deleting a specific record by using the method deleteById() of CrudRepository
    public void delete(int id) {
        commentRepository.deleteById(id);
    }
}
