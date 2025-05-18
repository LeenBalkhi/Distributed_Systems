package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface CommentRepository  extends CrudRepository<Comment, Integer> {

    Comment save(Comment comment);
    void deleteById(int var1);


}
