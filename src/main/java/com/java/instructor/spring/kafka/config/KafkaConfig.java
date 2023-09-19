package com.java.instructor.spring.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.google.gson.Gson;

@Configuration
public class KafkaConfig {
	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topic1() {
		return new NewTopic("TestTopic", 1, (short) 1);
	}
	
	// Producer configuration

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate(producerFactory());
	}
	
	
	// consumer configuration
	
	 @Bean
	    public ConsumerFactory<String, String> consumerFatory() {
	        Map<String, Object> config = new HashMap();
	        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
	        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        config.put(ConsumerConfig.GROUP_ID_CONFIG, "myGroupId");
	        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
	        return new DefaultKafkaConsumerFactory(config, new StringDeserializer(), new StringDeserializer());
	    }
	    @Bean
	    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
	        ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory();
	        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFatory());
	        concurrentKafkaListenerContainerFactory.setMissingTopicsFatal(false);
	        return concurrentKafkaListenerContainerFactory;
	    }
	    @Bean
	    public Gson gsonJsonConverter() {
	        return new Gson();
	    }
}
