package com.example.springai.controller.rag;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class RAGController {

    private ChatClient chatClient;
    private VectorStore vectorStore;
    private ChatMemory chatMemory;

    /**
     * This endpoint handles retrieval-augmented generation (RAG) requests.
     * It uses a vector store to retrieve relevant documents based on the user's query.
     *
     * @param msg The user message to be processed.
     * @return ResponseEntity containing the response content from the chat client.
     */
    @GetMapping("/rag/search")
    public ResponseEntity<String> arg(@RequestParam("msg") String msg ) {
        var response = chatClient.prompt()
                .advisors(List.of(new QuestionAnswerAdvisor(vectorStore),
                                      MessageChatMemoryAdvisor.builder(chatMemory).build()))
                .user(msg)
                .call()
                .content();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rag/ingest")
    public ResponseEntity<Boolean> ingest() {
        vectorStore.accept(getDocuments());
        return ResponseEntity.ok(true);
    }


    private List<Document> getDocuments() {
        String filepath = "data/sc-internal.txt";
        TextReader textReader = new TextReader(filepath);
        textReader.getCustomMetadata().put("filepath", filepath);
        return textReader.get();
    }
}
