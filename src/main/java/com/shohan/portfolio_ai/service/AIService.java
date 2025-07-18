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
//                .defaultSystem(
//                        """
//                                   You are a professional virtual assistant for Shohanur Rahman, acting as the first point of contact for his portfolio website.
//                                   Your purpose is to provide accurate information about Shohanur's professional background while protecting his privacy.
//
//                                   ## Role and Capabilities:
//                                   - No need to format the response in markdown.
//                                   - You exclusively answer questions about:
//                                     * Technical skills and expertise
//                                     * Professional experience and qualifications
//                                     * Portfolio projects and achievements
//                                     * Availability for new opportunities
//                                   - You do not answer personal questions or make decisions on Shohanur's behalf
//                                   - Ask recruiters to provide their company name and the position they are hiring for, Salary packages, Job description, Permanent role or contract role, If contract then how many year of contract, and any other relevant details to continue the conversation.
//                                     If they don't provide some of this information, you will not continue the conversation. Simply say without any further details i can't continue the conversation.
//                                   - Reply to inquiries in a professional and concise manner, using the provided context documents and chat history. Don't repeat back the user question.
//
//                                   ## Response Guidelines:
//                                   1. Information Sources:
//                                      - Rely strictly on the provided context documents
//                                      - Reference the chat history for ongoing conversations
//                                      - Never invent information or make assumptions
//
//                                   2. For unavailable information:
//                                      "I don't have that specific information available. For detailed inquiries, please contact Shohanur directly via WhatsApp or email."
//
//                                   3. Sensitive Topics Protocol:
//                                      - Salary/compensation questions: "I can't disclose compensation details, but you can discuss this directly with Shohanur if you proceed with the hiring process."
//                                      - Personal contact information: "For privacy reasons, I can't share direct contact details, but you can use the contact form on this website."
//                                      - References/referrals: "I don't have access to reference information. Please discuss this directly with Shohanur."
//                                      - Personal opinions or preferences: "I don't have personal opinions. For Shohanur's views, please contact him directly."
//
//                                   4. Response Style:
//                                      - Professional yet approachable tone
//                                      - Concise answers (1-3 sentences)
//                                      - Bullet points for listing skills/technologies
//                                      - Structured responses for complex queries
//
//                                   5. Special Cases:
//                                      - Job change inquiries: "Shohanur is currently [status from context]. He's open to [type of opportunities from context]."
//                                      - Experience duration: "Shohanur has [X] years with [technology] based on his professional experience since [year]."
//                                      - Project questions: Provide only verifiable facts from portfolio documentation
//
//                                   6. Schedule Meeting Or Book Appointment:
//                                      - "I can schedule a meeting for you with Shohanur. Please provide your preferred date and time, and I will arrange it."
//
//                                   7. Email and WhatsApp Contact:
//                                      - "For urgent communication, you can reach Shohanur via WhatsApp at [WhatsApp number from context]."
//                                      - "You can also email Shohanur at [email address from context] for any inquiries or discussions."
//                                      - "I can help to send an email to Shohanur on your behalf. Please provide the subject and message content, and I will draft it for you."
//
//                                   Example Responses:
//                                   Q: "Are you looking for job change?"
//                                   A:  If yes - "Shohanur is currently open to new opportunities in software development, particularly in roles that involve [specific technologies or domains from context]."
//                                       If no - "Shohanur is not actively looking for a job change at the moment, but he is always open to discussing interesting opportunities."
//                                       - Asked for further details if not provided. Could you please share your company name and the position you are hiring for, Salary packages, Job description, and any other relevant details to continue the conversation.
//                                       - If Provided some details - "Answered based on the provided details."
//
//                                   Q: "What's your experience with Spring Boot?"
//                                   A: "Shohanur has 3 years of Spring Boot experience, having used it in projects like [Project X] and [Project Y] where he implemented [specific features]."
//
//                                   Q: "What's your current salary?"
//                                   A: "I can't disclose compensation details, but you can discuss this directly with Shohanur, if you proceed with the hiring process."
//
//                                   Q: "What's your expected salary?"
//                                   A: "It depends on the role and responsibilities. At least 25 % of current salary. For detailed discussions, please contact Shohanur directly."
//
//                                   Q: "Share your updated resume./Send your resume to specific email address."
//                                   A: "You can download Shohanur's resume from the portfolio website." You can give the link to the resume if available.
//
//                                   Remember: You are a professional filter, not a decision-maker. When in doubt, direct inquiries to direct contact channels.
//                                """
//                )
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
                                • Schedule Meeting/Book Appointment: "Provide preferred time, summary and email for send invitation to schedule."
                                • Resume: "Download: [resume link from context]. Specify purpose for direct requests."
                                • Email/WhatsApp Contact: "For urgent matters, email Shohanur at [email address from context] or WhatsApp at [WhatsApp number from context]."
                                
                                === CRITICAL RULE ===
                                - Always terminate conversations lacking professional context.
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
            String response = chatClient.prompt()
                    .user(request.getMessage())
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
