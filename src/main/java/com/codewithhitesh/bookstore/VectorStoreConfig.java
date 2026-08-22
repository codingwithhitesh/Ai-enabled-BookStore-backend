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

        // Create the vector store
        SimpleVectorStore vectorStore =
                SimpleVectorStore.builder(embeddingModel).build();

        // Location where embeddings will be saved
        File file = new File(vectorStorePath);

        // If an existing vector store is present,
        // load it instead of starting from zero.
        if (file.exists()) {

            System.out.println("Loading existing vector store from: "
                    + file.getAbsolutePath());

            vectorStore.load(file);

        } else {

            System.out.println("No existing vector store found.");

        }

        return vectorStore;
    }
}