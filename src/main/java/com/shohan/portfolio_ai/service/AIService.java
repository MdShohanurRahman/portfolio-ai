package com.shohan.portfolio_ai.service;

import com.shohan.portfolio_ai.dto.AIPromptResponse;
import com.shohan.portfolio_ai.dto.PromptRequest;
import com.shohan.portfolio_ai.tools.AppointmentTool;
import com.shohan.portfolio_ai.tools.DateTimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.StringTokenizer;

@Service
@Slf4j
public class AIService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final DateTimeTool dateTimeTool;
    private final AppointmentTool appointmentTool;

    public AIService(
            AppointmentTool appointmentTool,
            DateTimeTool dateTimeTool,
            ChatClient chatClient,
            ChatMemory chatMemory,
            @Qualifier("portfolioVectorStore") VectorStore vectorStore
    ) {
        this.appointmentTool = appointmentTool;
        this.chatMemory = chatMemory;
        this.dateTimeTool = dateTimeTool;
        this.chatClient = chatClient.mutate()
                .defaultAdvisors(
                        List.of(
                                new QuestionAnswerAdvisor(vectorStore),
                                MessageChatMemoryAdvisor.builder(chatMemory).build()
                        )
                )
                .defaultSystem(
                        """
                                You are a professional virtual assistant for Shohanur Rahman's portfolio website.
                                Provide accurate professional information while protecting privacy. Never disclose personal/sensitive data.
                                Always answer based on the provided context documents.
                                
                                === RESPONSE SCOPE ===
                                ✅ Answer:
                                - Technical skills & expertise
                                - Professional experience
                                - Portfolio projects
                                - Opportunity availability (with conditions)
                                - Always answer with professional context (1-3) sentences
                                
                                ❌ Never answer:
                                - Personal questions
                                - Opinions/preferences
                                - Confidential data (contacts, salary, references)
                                - You do not answer personal questions or make decisions on Shohanur's behalf
                                
                                === STRICT PROTOCOLS ===
                                1. FOR JOB INQUIRIES REQUIRE IF NOT PROVIDED:
                                   • Company name
                                   • Position title
                                   • Job description
                                   • Employment type (Permanent/Contract)
                                   • Salary range
                                   • Contract duration (if applicable)
                             
                                2. INFORMATION HANDLING:
                                - Compensation: "Discuss directly during hiring process."
                                - Contacts: "Use website form or provide your details first."
                                - Unknown info: "Contact Shohanur directly via [channels]."
                                
                                3. RESPONSE STYLE:
                                - Professional yet approachable
                                - Start conversation why you are here and topics you can answer
                                - Grow conversation with relevant context smoothly
                                - No need to mention Based on the provided context documents, but always answer based on the provided context documents.
                                
                                4. CURRENT DATE AND TIME:
                                - Use dateTimeTool for current date/time
                                
                                === RESPONSE FORMAT ===
                                - Direct answer for direct question. not repeating user question
                                - Relevant portfolio context
                                - Professional yet approachable tone
                                - Bullet points for listing skills/technologies/required information
                                - For formating response style use markdown
                                - Structured responses for complex queries
                                - Next steps if needed
                                
                                === EXAMPLE RESPONSES ===
                                Q: "Spring Boot experience?"
                                A: "3 years professional experience. Built [X] at [Project Y] using Spring Security and Data JPA."
                                
                                Q: "Open to work?"
                                A: "Currently [status]. Interested in [roles]. Share position details to proceed."
                                
                                === SPECIAL FUNCTIONS ===
                                • Schedule Meeting/Book Appointment: "Provide preferred time, summary and email for invitation."
                                • Resume: "Download: [resume link from context]. Specify purpose for direct requests."
                                • Email/WhatsApp Contact: "For urgent matters, email Shohanur at [email address from context] or WhatsApp at [WhatsApp number from context]."
                                
                                === CRITICAL RULE ===
                                - Always terminate conversations lacking professional context.
                                - Always schedule meeting with provided functions when requested, not just answer.
                                - You are a professional filter, not a decision-maker. When in doubt, direct inquiries to direct contact channels.
                                """
                )
                .build();
    }

    /**
     * Processes a prompt request and returns an AI response.
     *
     * @param request The prompt request containing conversation ID and message.
     * @return The AI response containing the reply and conversation ID.
     * @throws IllegalArgumentException if the request is invalid.
     */
    public AIPromptResponse ask(PromptRequest request) {
        if (request.getConversationId() == null || request.getConversationId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversation ID must be provided.");
        }
        if (request.getMessage() == null || request.getMessage().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message must be provided.");
        }
        if (request.getConversationId().length() > 36) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversation ID must be 36 characters or less.");
        }
        if (!isWithinTokenLimit(request.getMessage(), 1500)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message exceeds the maximum token limit of 1500 tokens.");
        }
        try {
            String promptMessage = request.getMessage().trim();
            String additionalContext = """
                     Additional Info:
                       - current date and time: {currentDateTime}
                    """;
            String response = chatClient.prompt()
                    .user(userSpec -> {
                        userSpec.text(promptMessage + additionalContext);
                        userSpec.param("currentDateTime", dateTimeTool.getCurrentDateTime());
                    })

                    .advisors(
                            advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, request.getConversationId())
                    )
                    .tools(dateTimeTool, appointmentTool)
                    .call()
                    .content();

            AIPromptResponse aiPromptResponse = new AIPromptResponse();
            aiPromptResponse.setReply(response);
            aiPromptResponse.setConversationId(request.getConversationId());
            return aiPromptResponse;
        } catch (Exception e) {
            log.error("Error processing AI request: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage(), e);
        }
    }


    /**
     * Checks if the prompt message exceeds the maximum token limit.
     *
     * @param prompt The input prompt message.
     * @param maxTokens The maximum allowed tokens.
     * @return true if the token count is within the limit, false if it exceeds.
     */
    public boolean isWithinTokenLimit(String prompt, int maxTokens) {
        return countTokens(prompt) <= maxTokens;
    }

    /**
     * Counts the number of tokens in a prompt message.
     * Tokens are approximated by splitting on whitespace.
     *
     * @param prompt The input prompt message.
     * @return The number of tokens in the prompt.
     */
    public int countTokens(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return 0;
        }
        StringTokenizer tokenizer = new StringTokenizer(prompt);
        return tokenizer.countTokens();
    }

    public void clearChatHistory(String conversationId) {
        chatMemory.clear(conversationId);
    }
}
