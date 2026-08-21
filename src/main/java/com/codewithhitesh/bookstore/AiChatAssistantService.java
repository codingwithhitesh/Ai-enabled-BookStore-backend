// AUTO MEMORY   ***********************************************************************

package com.codewithhitesh.bookstore;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiChatAssistantService {

    private final ChatClient chatClient;
    private final UserRepository userRepository;
    private final BookTool bookTool;

    public AiChatAssistantService(ChatModel chatModel,
                                  UserRepository userRepository,
                                  BookTool bookTool) {

        this.userRepository = userRepository;
        this.bookTool = bookTool;

        // 1. Instantiation for 1.0.0-M6
        ChatMemory chatMemory = new InMemoryChatMemory();

        // 2. Pass 'chatMemory' to the builder
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chatWithCustomerAutoMemory(int userId, String userMessage) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "User not found.";
        }

        return chatClient.prompt()
                .system("""
                        You are a polite customer support bot for Jaipur Book Store.

                        Store Information
                        Location : Chaura Rasta Jaipur
                        CEO : Shruti Rathore

                        Customer Information
                        Name : %s
                        Status : %s
                        Total Orders : %d

                        Use customer information while answering.
                        Keep answers short.
                        CRITICAL: Whenever a customer asks about book availability, titles, or prices,
                        you MUST call the searchBooks tool to check the store database before responding.
                        Do not answer from memory.
                        """
                        .formatted(
                                user.getUserName(),
                                user.getUserStatus(),
                                user.getOrder().size()
                        ))
                .tools(bookTool)    // Tool Calling
                .user(userMessage)
                // 3. Use correct Spring AI 1.0.0-M6 key constant
                .advisors(a -> a.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, String.valueOf(userId)))
                .call()
                .content();
    }

    public String askGeneralAssistant(String message) {

        return chatClient.prompt()
                .system("""
                        You are Jaipur Book Store assistant.

                        Working Hours
                        Monday-Friday
                        9AM-4PM

                        If question is outside bookstore,
                        politely decline.
                        """)
                .tools(bookTool)
                .user(message)
                .call()
                .content();
    }
}


// ollama pull llama3.2  (To start Ollama , paste this command in commamd prompt)
//Postman URL: http://localhost:8080/Jaipur-BookStore-online/ai/GeneralChat?message=What are your working hours?
//Postman URL: http://localhost:8080/Jaipur-BookStore-online/ai/customer/{id}/askAi"


// MANUAL MEMORY  *********************************************************************

/*package com.codewithhitesh.bookstore;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AiChatAssistantService {

    private final ChatModel chatModel;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    public AiChatAssistantService(ChatModel chatModel,
                                  UserRepository userRepository,
                                  ChatMessageRepository chatMessageRepository) {

        this.chatModel = chatModel;
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public String chatWithCustomerManualMemory(int userId, String userMessage) {

        // 1. Fetch user from H2
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "Hello! I couldn't find an account matching that ID.";
        }

        // 2. Load latest 10 messages
        List<ChatMessage> history =
                chatMessageRepository.findTop10ByUserIdOrderByTimestampDesc(userId);

        // Reverse because DESC returns newest first
        Collections.reverse(history);

        // 3. Build conversation history
        StringBuilder historyBuilder = new StringBuilder();

        for (ChatMessage msg : history) {

            historyBuilder.append(msg.getSender())
                    .append(": ")
                    .append(msg.getContent())
                    .append("\n");
        }

        // 4. Create prompt
        String fullPromptText = """
                You are a polite, concise customer support bot for Jaipur Book Store Online.

                BOOK STORE INFORMATION

                Location : Chaura Rasta, Jaipur

                Established : 1972

                CEO : Shruti Rathore

                DATABASE INFORMATION

                Customer Name : %s

                Customer Status : %s

                Total Orders : %s

                PREVIOUS CONVERSATION

                %s

                CURRENT CUSTOMER QUESTION

                USER: %s

                INSTRUCTION

                Use both the database information and previous conversation.

                If the answer is present in previous conversation, use it.

                Keep answers short and friendly.
                """
                .formatted(
                        user.getUserName(),
                        user.getUserStatus(),
                        user.getOrder().size(),
                        historyBuilder.length() == 0
                                ? "No previous conversation."
                                : historyBuilder.toString(),
                        userMessage
                );

        // 5. Ask Llama
        String aiResponse = chatModel.call(fullPromptText);

        // 6. Save current user message
        chatMessageRepository.save(
                new ChatMessage(userId, "USER", userMessage)
        );

        // 7. Save AI response
        chatMessageRepository.save(
                new ChatMessage(userId, "ASSISTANT", aiResponse)
        );

        return aiResponse;
    }


    // General chatbot (without memory)

    public String askGeneralAssistant(String userPrompt) {

        String prompt = """
                You are a polite customer support bot for Jaipur Book Store Online.

                Location : Chaura Rasta Jaipur

                Working Hours:
                Monday-Friday
                9 AM to 4 PM

                If question is outside bookstore,
                politely decline.
                """;

        return chatModel.call(prompt + "\n\nQuestion:\n" + userPrompt);
    }

}

*/

// ollama pull llama3.2  (To start Ollama , paste this command in commamd prompt)
//Postman URL: http://localhost:8080/Jaipur-BookStore-online/ai/GeneralChat?message=What are your working hours?
//Postman URL: http://localhost:8080/Jaipur-BookStore-online/ai/customer/{id}/askAi"



