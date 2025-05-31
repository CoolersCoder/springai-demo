package com.example.springai.controller.image;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
@AllArgsConstructor
@RestController
public class ImageController {

    ImageModel imageModel;

    @GetMapping("/blocking/image")
    public void ImageController(@RequestParam(value="msg", defaultValue = "A light cream colored mini golden doodle") String message, HttpServletResponse rsesponse) {
        ImageResponse image = imageModel.call(new ImagePrompt(message,
                         OpenAiImageOptions.builder()
                        .quality("hd")
                        .height(1024)
                        .width(1024).build()));
        setRespImage(rsesponse, image);
    }

    private void setRespImage(HttpServletResponse rsesponse, ImageResponse image) {
        try {
            var url = URI.create(image.getResult().getOutput().getUrl()).toURL();
            rsesponse.setContentType("image/png");
            rsesponse.setHeader("Content-Disposition", "inline; filename=image.png");
            url.openStream().transferTo(rsesponse.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("Failed to write image to response", e);
        }
    }
}
