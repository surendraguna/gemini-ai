package gemini.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gemini.ai.service.GenerativeAIService;

@RestController
@RequestMapping("/generative-ai")
public class GenerativeAIController {

    @Autowired
    private GenerativeAIService generativeAIService;

    @GetMapping("/{userInput}")
    public String generateContent(@PathVariable String userInput) {
        return generativeAIService.generateContent(userInput);
    }
}
