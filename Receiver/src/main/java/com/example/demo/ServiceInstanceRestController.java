package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
class ServiceInstanceRestController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
    @RequestMapping(value ="/getcomment", method = RequestMethod.GET)
    public CommentList getComment() {

        return restTemplate.getForObject("http://localhost:8083/comments/comments" ,CommentList.class);
    }

    @RequestMapping(value = "/getSchoolDetails", method = RequestMethod.GET)
    public String getStudents(@PathVariable String schoolname) {
        System.out.println("Getting School details for " + schoolname);
        String response = restTemplate.exchange("http://student-service/getStudentDetailsForSchool/{schoolname}"
                , HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }, schoolname).getBody();

        System.out.println("Response Received as " + response);

        return "School Name -  " + schoolname + " \n Student Details " + response;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}