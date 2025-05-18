package com.example.demo;

import com.example.demo.payload.UploadFileResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
class ServiceInstanceRestController {

    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private RestTemplate restTemplate;

    ServiceInstanceRestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }



    @PostMapping("/uploadMultipleFiles/{fileName}")
    public UploadFileResponse uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @PathVariable String fileName) throws IOException {
        rabbitTemplate.convertAndSend(Test1Application.topicExchangeName, "foo.bar.baz",fileName);
        rabbitTemplate.convertAndSend(Test1Application.topicExchangeName, "foo.bar.baz",files.length);

        for (int i = 0; i <files.length ; i++) {
            rabbitTemplate.convertAndSend(Test1Application.topicExchangeName, "foo.bar.baz",files[i].getBytes());
        }

         return new UploadFileResponse(fileName,fileName,fileName,files[0].getSize());
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}