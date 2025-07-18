package com.shohan.portfolio_ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PromptRequest {
    @Schema(
            description = "Message content",
            example = "Hi Shohanur,\\n\\nHope this message finds you well...",
            type = "string"
    )
    private String message;

    @Schema(
            description = "Conversation ID for context",
            example = "conv_123456"
    )
    private String conversationId;
}
