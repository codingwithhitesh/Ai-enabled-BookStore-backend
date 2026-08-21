package com.codewithhitesh.bookstore;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookstoreRagAssistantService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public BookstoreRagAssistantService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    public String answerStorePolicyQuestion(String customerQuery) {

        // 1. Semantic Search using builder pattern in 1.0.0-M6
        SearchRequest searchRequest = SearchRequest.builder()
                .query(customerQuery)
                .topK(3)
                .similarityThreshold(0.6)
                .build();

        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);

        // 2. Combine matched text chunks into one context string
        String pdfContext = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));

        // 3. Augment system prompt with PDF content and query LLM
        return chatClient.prompt()
                .system(s -> s.text("""
                        You are a helpful customer support bot for Jaipur Book Store.
                        Answer the customer's question ONLY using the official store documents provided below.
                        If the answer is not in the documents, state politely that you do not know.

                        OFFICIAL DOCUMENTS CONTEXT:
                        {context}
                        """).param("context", pdfContext))
                .user(customerQuery)
                .call()
                .content();
    }
}