package com.example.springai.controller.multimodality;

import com.example.springai.model.ImageDetails;
import com.example.springai.model.ImageVerificationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;
@AllArgsConstructor
@RestController
public class MultimodalityController {

    private final ChatClient chatClient;

    /**
     * This endpoint allows you to verify if a delivery photo is correct.
     * It uses a blocking call to the chat client to process the image.
     *
     * @param request The request containing the image URL for verification.
     * @return ResponseEntity containing ImageDetails with verification results.
     */
    @PostMapping("/blocking/image/verification")
    public ResponseEntity<ImageDetails> blockingChatImage(@Valid @RequestBody ImageVerificationDTO request) {
        var imageVerificationResponse = this.chatClient
                .prompt()
                .system(system -> {
                    system.text("You are package delivery system operator and check delivery photo if it is delivery to right place");
                })
                .user(usermessage -> {
                    try {
                        usermessage.text("return true or false for isDelivery and short description of the image")
                                .media(MimeTypeUtils.IMAGE_JPEG, URI.create(request.getUrl()).toURL());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .call()
                .entity(ImageDetails.class);
        return ResponseEntity.ok(imageVerificationResponse);
    }
}
