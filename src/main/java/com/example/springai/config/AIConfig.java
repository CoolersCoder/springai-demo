package com.example.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        // create a vector store with the provided embedding model
       var vectorStore = SimpleVectorStore.builder(embeddingModel).build();
       // read documents from a file, split them into smaller chunks, and add them to the vector store
       var documents = getDocuments();
       // split the documents into smaller chunks using TokenTextSplitter
       TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
       tokenTextSplitter.apply(documents);
       vectorStore.add(documents);
       return vectorStore;
    }

    private List<Document> getDocuments() {
        String filepath = "data/sc-internal.txt";
        TextReader textReader = new TextReader(filepath);
        textReader.getCustomMetadata().put("filepath", filepath);
        return textReader.get();
    }

}
