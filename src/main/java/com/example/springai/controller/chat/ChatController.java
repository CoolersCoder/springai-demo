package com.example.springai.controller.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
