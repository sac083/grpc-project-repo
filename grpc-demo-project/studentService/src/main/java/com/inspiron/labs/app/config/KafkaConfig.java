package com.inspiron.labs.app.config;

import com.inspiron.labs.app.constants.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public NewTopic topic(){
        return TopicBuilder.name(AppConstants.SAVE_STUDENT_OBJECT).partitions(2).replicas(1).build();
    }
}
