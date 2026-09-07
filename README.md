🚀 AI-Powered Bookstore Backend with Customer Chat, Memory, Tool Calling & RAG



Excited to share a major upgrade to my Spring Boot Online Bookstore project!

What started as a traditional e-commerce backend gradually evolved into an experiment combining Spring Boot, Spring AI, Ollama, Tool Calling, Conversation Memory, and RAG.

The core idea I wanted to explore was:

How can an AI assistant interact directly with live backend data AND retrieve information from unstructured policy documents instead of guessing or hallucinating answers?

🏗️ Core Backend Built With
Java 21
Spring Boot
Spring Data JPA / Hibernate
H2 In-Memory Database
REST APIs
User & Book management
Cart & Order processing
Pagination & Sorting using Pageable
Global Exception Handling
🤖 Main Feature: Multi-Faceted AI Chat System

I implemented targeted AI chat experiences using Spring AI and local LLMs through Ollama.

1️⃣ Customer-Specific Chat — Memory & Context

A customer can chat using their customer ID.

The AI automatically receives real-time context about that specific customer:

👤 Customer name
📊 Customer status
🛒 Total order count

Conversation history is maintained using:

MessageChatMemoryAdvisor
InMemoryChatMemory

This allows the assistant to maintain context across multiple messages.

Flow
Customer
   ↓
Spring Boot
   ↓
ChatClient + Conversation Memory
   ↓
Customer Context from Database
   ↓
AI Model
   ↓
Context-Aware Response
2️⃣ General Bookstore Assistant

I also implemented a general-purpose bookstore assistant for public customer questions such as:

Store working hours
Store location
General bookstore information
Basic policies
Bookstore-related questions

The assistant is designed with guardrails to decline questions outside the bookstore domain.

🔧 AI Tool Calling / Function Calling

One of the important concepts I wanted to explore was preventing the LLM from guessing database information.

For example, if a customer asks:

"Is 'Effective Java' available in stock and what does it cost?"

Instead of relying on the model's knowledge, the AI can dynamically invoke backend tools.

Flow
Customer Question
       ↓
     AI Model
       ↓
   Tool Calling
       ↓
     BookTool
       ↓
 UserBookService
       ↓
   H2 Database
       ↓
 Live Book Information
       ↓
     AI Response
Currently Active Tools

📚 getBookAvailability

Checks the live database to determine whether a particular book is available in stock.

💰 getBookPrice

Queries the current selling price directly from the bookstore inventory.

This approach allows the AI to work with real-time application data instead of hallucinating database values.

📄 RAG — Retrieval-Augmented Generation with PDF Documents

This was one of the biggest improvements I made to the project.

Instead of hardcoding bookstore policies into prompts or relying on the LLM's general knowledge, I implemented a RAG pipeline using PDF documents, Ollama embeddings, and Spring AI's SimpleVectorStore.

The bookstore policies are maintained as actual documents, including:

📄 FAQ.pdf
📄 Issue policy.pdf
📄 Return policy.pdf

🔄 PDF → Embedding → Vector Search → AI Response

At application startup, the PDF documents are loaded and processed.

The documents are:

📥 Loaded into the application
✂️ Split into smaller chunks
🧠 Converted into vector embeddings
💾 Stored in a local SimpleVectorStore
🔎 Retrieved using similarity search when a user asks a policy-related question
🤖 Relevant document content is provided to the AI to generate a grounded response
RAG Pipeline
PDF Documents
     ↓
Document Loading
     ↓
Text Extraction
     ↓
Document Chunking
     ↓
Embedding Generation
     ↓
nomic-embed-text via Ollama
     ↓
SimpleVectorStore
     ↓
Similarity Search
     ↓
Relevant Document Chunks
     ↓
Spring AI
     ↓
Grounded AI Response

For example, when a customer asks:

"How long can I keep an issued book?"

the system performs a similarity search against the indexed policy documents and retrieves the relevant policy content before generating the answer.

This makes the response grounded in the bookstore's actual documents rather than depending purely on the LLM's general knowledge.

🧠 RAG Architecture
                 ┌──────────────────┐
                 │   Policy PDFs    │
                 │ FAQ / Return /   │
                 │ Issue Policies   │
                 └────────┬─────────┘
                          ↓
                 Document Ingestion
                          ↓
                    Chunking
                          ↓
                Ollama Embeddings
               (nomic-embed-text)
                          ↓
                 SimpleVectorStore
                          │
                          │ Similarity Search
                          ↓
Customer Question → Relevant Chunks
                          ↓
                    Spring AI
                          ↓
                     LLM / Ollama
                          ↓
                Grounded Response
🖥️ Working Frontend

I also completed a working frontend for the bookstore application, connecting the UI directly with the Spring Boot REST APIs.

The frontend allows users to interact with the main bookstore functionality without relying only on Postman.

Frontend Features

👤 User management
📚 Book management
🛒 Cart management
📦 Order placement
🤖 General AI Chat
🧠 Customer-specific AI Chat with Memory
📄 RAG / Policy-based AI interaction

The frontend communicates with the same REST endpoints exposed by the Spring Boot backend, allowing the application to be tested as an actual end-to-end system:

Frontend
    ↓
REST APIs
    ↓
Spring Boot
    ↓
Services
    ↓
JPA / H2

And for AI features:

Frontend
    ↓
Spring Boot
    ↓
Spring AI
    ├── Conversation Memory
    ├── Tool Calling
    └── RAG / Vector Search
           ↓
      Ollama / LLM

This was an important step for me because it moved the project beyond simply testing APIs through Postman and towards a complete working application with both backend and frontend interaction.

🧠 What I Learned

Through this project, I got hands-on experience with:

Spring AI ChatClient
Spring AI ChatModel
EmbeddingModel
VectorStore
Retrieval-Augmented Generation (RAG)
PDF document ingestion
Document chunking
Vector embeddings
Similarity search
Ollama
Local LLMs
llama3.1
nomic-embed-text
MessageChatMemoryAdvisor
InMemoryChatMemory
AI Tool Calling using @Tool
Connecting AI with live database information
Spring Data JPA / Hibernate
REST API development
Pagination & Sorting
H2 database
Frontend-to-backend integration
🔮 What's Next?

There are still several things I want to explore and improve:

🔐 Security & JWT Authentication
🧪 Integration testing for Tool Calling and RAG pipelines
📦 DTO mappings using MapStruct
🐳 Dockerization
☁️ Cloud Deployment
🗄️ Production-ready persistent Vector Database
📊 Better observability and monitoring for AI requests



#Java #SpringBoot #SpringAI #GenerativeAI #RAG #AI #BackendDevelopment #Ollama #VectorDatabase #LLM #ToolCalling #SpringDataJPA #Hibernate #RESTAPI #LearningInPublic
