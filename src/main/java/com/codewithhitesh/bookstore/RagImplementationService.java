package com.codewithhitesh.bookstore;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class RagImplementationService {

    private final SimpleVectorStore vectorStore;

    @Value("${spring.ai.vectorstore.simple.path:vectorstore.json}")
    private String vectorStorePath;

    public RagImplementationService(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void ingestPdfDocuments() {

        File vectorStoreFile = new File(vectorStorePath);

        /*
         * If vectorstore.json already exists,
         * VectorStoreConfig has already loaded it.
         *
         * Therefore we don't need to create embeddings again.
         */
        if (vectorStoreFile.exists()) {

            System.out.println(
                    "Existing vector store found. "
                            + "Skipping PDF ingestion."
            );

            return;
        }

        try {

            System.out.println("No vector store found.");
            System.out.println("Starting PDF ingestion...");

            // ---------------------------------------------------------
            // 1. Find PDFs inside resources/docs/
            // ---------------------------------------------------------

            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();

            Resource[] pdfResources =
                    resolver.getResources("classpath:docs/*.pdf");

            if (pdfResources.length == 0) {

                System.out.println(
                        "No PDF documents found in classpath:docs/"
                );

                return;
            }

            // ---------------------------------------------------------
            // 2. Process every PDF
            // ---------------------------------------------------------

            for (Resource pdfResource : pdfResources) {

                if (!pdfResource.exists()) {
                    continue;
                }

                System.out.println(
                        "Processing PDF: "
                                + pdfResource.getFilename()
                );

                // -----------------------------------------------------
                // 3. Read PDF
                // -----------------------------------------------------

                PdfDocumentReaderConfig config =
                        PdfDocumentReaderConfig.builder()
                                .withPageTopMargin(0)
                                .build();

                PagePdfDocumentReader pdfReader =
                        new PagePdfDocumentReader(
                                pdfResource,
                                config
                        );

                List<Document> rawDocuments =
                        pdfReader.get();

                System.out.println(
                        "Pages read: "
                                + rawDocuments.size()
                );

                // -----------------------------------------------------
                // 4. Split pages into smaller chunks
                // -----------------------------------------------------

                TokenTextSplitter textSplitter =
                        new TokenTextSplitter();

                List<Document> chunkedDocuments =
                        textSplitter.apply(rawDocuments);

                System.out.println(
                        "Chunks created: "
                                + chunkedDocuments.size()
                );

                // -----------------------------------------------------
                // 5. Create embeddings and store them
                // -----------------------------------------------------

                vectorStore.add(chunkedDocuments);

                System.out.println(
                        "Embeddings created for: "
                                + pdfResource.getFilename()
                );
            }

            // ---------------------------------------------------------
            // 6. IMPORTANT:
            //    Persist the vector store to disk
            // ---------------------------------------------------------

            File parentDirectory =
                    vectorStoreFile.getParentFile();

            if (parentDirectory != null) {
                parentDirectory.mkdirs();
            }

            vectorStore.save(vectorStoreFile);

            System.out.println(
                    "Vector store successfully saved at: "
                            + vectorStoreFile.getAbsolutePath()
            );

        } catch (IOException e) {

            System.err.println(
                    "Error reading PDF files during RAG ingestion: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}