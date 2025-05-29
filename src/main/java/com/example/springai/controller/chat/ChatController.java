package com.example.springai.controller.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

@RestController
public class ChatController {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;




    public ChatController(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    /**
     * This endpoint handles synchronous chat requests.
     * @param message The user message to be processed.
     * @return The response content from the chat client.
     */
    @GetMapping("/blocking/chat")
    public String blockingChat(@RequestParam(value="msg") String message) {
        return this.chatClient.prompt()
               // .advisors(new SimpleLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }

    /**
     * This endpoint handles asynchronous chat requests.
     * @param message The user message to be processed.
     * @return A Flux that emits the response content from the chat client.
     */
    @GetMapping(value = "/stream/chat", produces = "text/stream; charset=utf-8")
    public Flux<String> streamChat(@RequestParam(value="msg") String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }

    /**
     * This endpoint handles chat requests with memory.
     * @param message The user message to be processed.
     * @return The response content from the chat client with memory.
     */
    @GetMapping("/blocking/memory/chat")
    public String blockingMemoryChat(@RequestParam(value="msg") String message) {
        return this.chatClient.prompt()
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .user(message)
                .call()
                .content();
    }

    /**
     * This endpoint handles chat requests with a media file.
     * @return The response content from the chat client with media.
     */
    @GetMapping("/blocking/image/chat")
    public ImageDetails blockingChatImage() {
       return this.chatClient.prompt().user(usermessage -> {
           try {
               usermessage.text("Explain what do you see on this picture? return short description and content will be 1234567890")
                       .media(MimeTypeUtils.IMAGE_PNG, URI.create("https://docs.spring.io/spring-ai/reference/_images/multimodal.test.png").toURL());
           } catch (MalformedURLException e) {
               throw new RuntimeException(e);
           }
       }).call().entity(ImageDetails.class);
    }

    private record ImageDetails(String des, String content) {}

}
