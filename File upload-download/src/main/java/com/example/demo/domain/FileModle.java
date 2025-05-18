package com.example.demo.domain;


import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Data
@Entity
@Component
public class FileModle {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String Url;

    public FileModle() {
    }
    public FileModle(String name,String Url) {
        this.name=name;
        this.Url=Url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }




}



