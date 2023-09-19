package com.java.instructor.spring.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;
import com.java.instructor.spring.kafka.entity.Employee;

@Controller
public class ConsumerController {
	
	@Autowired
    private Gson gson;
    @KafkaListener(topics = { "TestTopic" })
    public void getTopics(@RequestBody String emp) {
        System.out.println("Kafka event consumed is: " + emp);
        Employee model = gson.fromJson(emp, Employee.class);
        System.out.println("Model converted value: " + model.toString());
    }

}
