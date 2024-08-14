package gemini.ai.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Service
public class GenerativeAIService {

    @Value("${google.api.key}")
    private String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";

    public String generateContent(String userInput) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        String requestJson = String.format("{\"contents\":[{\"role\":\"user\",\"parts\":[{\"text\":\"%s\"}]}]}", userInput);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());
                JsonNode candidatesNode = rootNode.path("candidates");
                
                if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                    JsonNode firstCandidate = candidatesNode.get(0);
                    JsonNode contentNode = firstCandidate.path("content");
                    JsonNode partsNode = contentNode.path("parts");

                    if (partsNode.isArray() && partsNode.size() > 0) {
                        JsonNode textNode = partsNode.get(0).path("text");
                        return textNode.asText();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Failed to generate content: " + response.getStatusCode());
        }
        return null;
    }
}
