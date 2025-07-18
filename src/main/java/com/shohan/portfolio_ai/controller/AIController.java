package com.shohan.portfolio_ai.controller;

import com.shohan.portfolio_ai.dto.AIPromptResponse;
import com.shohan.portfolio_ai.dto.PromptRequest;
import com.shohan.portfolio_ai.service.AIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/ai")
@Tag(name = "Portfolio Assistance", description = "Controller for portfolio assistance chat interactions")
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AIPromptResponse> ask(@RequestBody PromptRequest request) {
        return ResponseEntity.ok(aiService.ask(request));
    }

    @DeleteMapping("/history/{conversationId}")
    public ResponseEntity<Void> clearChatHistory(@PathVariable String conversationId) {
        aiService.clearChatHistory(conversationId);
        return ResponseEntity.noContent().build();
    }
}
