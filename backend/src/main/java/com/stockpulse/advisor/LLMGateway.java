package com.stockpulse.advisor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LLMGateway {
    
    private static final Logger logger = LoggerFactory.getLogger(LLMGateway.class);
    
    @Value("${llm.api.url}")
    private String apiUrl;
    
    @Value("${llm.api.key}")
    private String apiKey;
    
    @Value("${llm.model}")
    private String model;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public LLMGateway() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    public String callLLM(String prompt) throws Exception {
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            // Prepare request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            requestBody.put("messages", new Map[]{message});
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            logger.info("Calling LLM API with prompt: {}", prompt.substring(0, Math.min(prompt.length(), 100)) + "...");
            
            // Make the API call
            ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
            );
            
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("LLM API returned status: " + response.getStatusCode());
            }
            
            // Parse the response to extract the content
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode choicesNode = rootNode.path("choices");
            
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                JsonNode contentNode = messageNode.path("content");
                
                if (contentNode.isTextual()) {
                    String content = contentNode.asText();
                    logger.info("LLM API response received: {}", content.substring(0, Math.min(content.length(), 100)) + "...");
                    return content;
                }
            }
            
            throw new RuntimeException("Failed to parse LLM response");
            
        } catch (RestClientException e) {
            logger.error("LLM API call failed: ", e);
            throw new RuntimeException("LLM API call failed: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing LLM response: ", e);
            throw new RuntimeException("Error processing LLM response: " + e.getMessage(), e);
        }
    }
}