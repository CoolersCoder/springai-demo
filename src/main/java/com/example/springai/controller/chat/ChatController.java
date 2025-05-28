package com.example.springai.controller.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {
    private final ChatClient chatClient;
    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * This endpoint handles synchronous chat requests.
     * @param message The user message to be processed.
     * @return The response content from the chat client.
     */
    @GetMapping("/blocking/chat")
    public String generation(@RequestParam(value="msg") String message) {
        return this.chatClient.prompt()
//                .advisors(new SimpleLoggerAdvisor())
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
    public Flux<String> generation1(@RequestParam(value="msg") String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }

    @GetMapping("/blocking/chat/image")
    public String generationWithImage(@RequestParam(value="msg") String message, @RequestParam(value="image") String imageUrl) {
        return this.chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
