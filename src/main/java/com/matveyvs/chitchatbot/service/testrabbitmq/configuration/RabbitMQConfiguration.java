package com.matveyvs.chitchatbot.service.testrabbitmq.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.matveyvs.chitchatbot.service.testrabbitmq.RabbitQueue.*;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue textMessageQueue(){
        return new Queue(TEXT_MESSAGE_UPDATE);
    }
    @Bean
    public Queue callbackQueryMessage(){
        return new Queue(CALLBACK_QUERY_MESSAGE);
    }
    @Bean
    public Queue answerMessageQueue(){
        return new Queue(ANSWER_MESSAGE);
    }
    @Bean
    public Queue deleteMessageQueue(){
        return new Queue(DELETE_MESSAGE);
    }
    @Bean
    public Queue callbackAnswerMessageQueue(){
        return new Queue(CALLBACK_ANSWER_MESSAGE);
    }
}
