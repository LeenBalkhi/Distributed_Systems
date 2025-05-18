package com.example.demo.Repository;

import com.example.demo.domain.FileModle;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<FileModle, Integer> {

    FileModle save(FileModle comment);
    FileModle findByName(String Name);

    void deleteById(int var1);


}
