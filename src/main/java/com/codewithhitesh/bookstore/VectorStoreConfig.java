package com.codewithhitesh.bookstore;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.ai.vectorstore.simple.path:vectorstore.json}")
    private String vectorStorePath;

    @Bean
    public SimpleVectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File file = new File(vectorStorePath);

        // Load existing embeddings if the JSON file exists
        if (file.exists()) {
            vectorStore.load(file);
        }

        return vectorStore;
    }
}