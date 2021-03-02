package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {


    @Autowired
    RestTemplate restTemplate;

    //使用以下方式仍可以注入restTemplate
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
       return restTemplateBuilder.build();
    }

    //也可使用@LoadBalanced
    @Autowired
    LoadBalancerClient loadBalancerClient;


    @GetMapping("user/{id}")
    public String getOrders(@PathVariable("id") int id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("spring-cloud-order-service");
        String url = String.format("http://%s:%s/", serviceInstance.getHost(), serviceInstance.getPort() + "/orders");
        //return restTemplate.getForObject("http://localhost/orders",String.class);
        return restTemplate.getForObject(url, String.class);
    }
}
