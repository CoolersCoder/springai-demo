package com.example.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    @Bean
    public ChatClient initChatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    @Bean
    @ConditionalOnMissingBean(ChatMemory.class)
    public MessageChatMemoryAdvisor customizeMessageChatMemory(){
        return MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().maxMessages(100).build()).build();
    }

}
