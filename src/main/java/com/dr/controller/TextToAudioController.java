package com.dr.controller;

import com.dr.service.ElevenLabsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;

@Controller
@RequestMapping("/text-to-audio")
public class TextToAudioController {

    @Autowired
    private ElevenLabsService elevenLabsService;

    @GetMapping
    public String showForm() {
        return "text_to_audio";
    }

    @PostMapping("/convert")
    public String convertTextToAudio(
            @RequestParam("text") String text,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileLocation") String fileLocation,
            Model model) {
        try {
            byte[] audioBytes = elevenLabsService.convertTextToSpeech(text);
            if (audioBytes.length > 0) {
                File outputFile = new File(fileLocation, fileName + ".mp3");
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(audioBytes);
                }
                model.addAttribute("success", true);
                model.addAttribute("message", "Audio file saved successfully!");
                model.addAttribute("filePath", outputFile.getAbsolutePath());
            } else {
                model.addAttribute("failed", true);
                model.addAttribute("message", "Failed to convert text to audio. Please try again.");
            }
        } catch (Exception e) {
            model.addAttribute("failed", true);
            model.addAttribute("message", "An error occurred: " + e.getMessage());
        }
        return "text_to_audio";
    }
}
