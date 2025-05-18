package com.example.demo;


import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.stereotype.Component;

import javax.persistence.*;



@Data
@Entity
@Component
public class Comment  {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String message;


    public Comment() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Comment(String message) {
        this.message = message;

    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}



