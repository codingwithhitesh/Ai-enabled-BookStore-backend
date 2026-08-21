package com.codewithhitesh.bookstore;



import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class RagImplementationService {

    private final VectorStore vectorStore;

    public RagImplementationService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void ingestPdfDocuments() {
        try {
            // 1. Locate PDF files from src/main/resources/docs/
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] pdfResources = resolver.getResources("classpath:docs/*.pdf");

            if (pdfResources == null || pdfResources.length == 0) {
                System.out.println("No PDF documents found in classpath:docs/");
                return;
            }

            for (Resource pdfResource : pdfResources) {
                if (!pdfResource.exists()) {
                    continue;
                }

                // 2. Read PDF using Spring AI PagePdfDocumentReader
                PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .build();

                PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, config);
                List<Document> rawDocuments = pdfReader.get();

                // 3. Chunk large text using default TokenTextSplitter constructor
                TokenTextSplitter textSplitter = new TokenTextSplitter();
                List<Document> chunkedDocuments = textSplitter.apply(rawDocuments);

                // 4. Save embeddings into Vector Store
                vectorStore.add(chunkedDocuments);
                System.out.println("Ingested " + pdfResource.getFilename() + " (" + chunkedDocuments.size() + " chunks)");
            }
        } catch (IOException e) {
            System.err.println("Error reading PDF files during RAG ingestion: " + e.getMessage());
        }
    }
}