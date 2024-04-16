package com.inspiron.labs.app.config;
import com.inspiron.labs.app.constants.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaConfig {
    public NewTopic topic(){
        return TopicBuilder.name(AppConstants.SEND_NOTIFICATION_OBJECT).build();
    }
}