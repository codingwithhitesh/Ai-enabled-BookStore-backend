package com.codewithhitesh.bookstore;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.util.List;

@Configuration
public class VectorStoreConfig {

    // For rendering via Grok , Dummy bean created
    @Bean
    @Primary
    public EmbeddingModel embeddingModel() {
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                return new EmbeddingResponse(List.of(new Embedding(new float[1536], 0)));
            }

            @Override
            public float[] embed(String text) {
                return new float[1536];
            }

            @Override
            public float[] embed(Document document) {
                return new float[0];
            }
        };
    }

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