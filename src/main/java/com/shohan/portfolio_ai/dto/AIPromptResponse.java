package com.shohan.portfolio_ai.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AIPromptResponse {
    private String reply;
    private String conversationId;
}