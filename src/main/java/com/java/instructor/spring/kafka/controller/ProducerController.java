package com.java.instructor.spring.kafka.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.java.instructor.spring.kafka.entity.Employee;

@RestController
public class ProducerController {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private Gson gson;

	@PostMapping("/produce")
	public ResponseEntity<String> postModelToKafka(@RequestBody Employee emp)
			throws InterruptedException, ExecutionException {
		CompletableFuture<SendResult<String, String>> result = (CompletableFuture<SendResult<String, String>>) kafkaTemplate
				.send("TestTopic", gson.toJson(emp));
		return new ResponseEntity(result.get().getProducerRecord().value(), HttpStatus.OK);
	}
}