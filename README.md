🚀 AI-Powered Bookstore Backend with Customer Chat, Memory, Tool Calling & RAG
Excited to share a major upgrade to my Spring Boot Online Bookstore project!
What started as a traditional e-commerce backend gradually evolved into an experiment combining Spring Boot, Spring AI, and Ollama.

The core idea I wanted to explore was:
How can an AI assistant interact directly with the backend database AND retrieve information from unstructured policy documents instead of hallucinating answers?

🏗️ Core Backend
Built with:

Java 21 & Spring Boot

Spring Data JPA / Hibernate

H2 In-Memory Database

REST APIs (User/Book management, Cart & Order processing)

Pagination & Sorting (Pageable)

Global Exception Handling

🤖 Main Feature: Multi-Faceted AI Chat System
I implemented targeted chat experiences powered by Spring AI and local LLMs:

1️⃣ Customer-Specific Chat (With Memory & Context)
A customer can chat using their customer ID. The AI automatically receives real-time context about that specific user:

👤 Name

📊 Customer status

🛒 Total order count

Conversation history is maintained using MessageChatMemoryAdvisor (with InMemoryChatMemory), enabling context-aware, multi-turn dialogs.


Customer → Spring Boot → ChatClient + Conversation Memory → Backend Context → Response
2️⃣ General Bookstore Assistant
A general customer support assistant designed to handle public store inquiries (working hours, location, policies) with strict guardrails to decline off-topic queries.

🔧 AI Tool Calling / Function Calling
To prevent the LLM from guessing database details, I implemented backend tools using Spring AI's @Tool annotation.

For example, when a user asks:

"Is 'Effective Java' available in stock and what does it cost?"

The AI dynamically invokes backend functions to query live data:


AI Model → BookTool → UserBookService → H2 Database → Spring AI → Customer Response
Currently Active Tools:

📚 getBookAvailability: Dynamically checks live database stock by book title.

💰 getBookPrice: Queries current selling prices directly from the store inventory.

📄 RAG (Retrieval-Augmented Generation) & Document Ingestion
Today (21 August 2026), I upgraded the AI pipeline by implementing RAG using Spring AI and local embeddings.

Instead of hardcoding store rules or relying on generic model training:

Document Ingestion: PDF files (FAQ.pdf, Issue policy.pdf, Return policy.pdf) are loaded into the application context at startup.

Vector Embeddings: Ingested documents are chunked and converted into vector embeddings using nomic-embed-text via Ollama.

Similarity Search: The vectors are stored in a local SimpleVectorStore. When users ask policy questions (e.g., return windows or book issuing rules), Spring AI performs a vector similarity search to retrieve relevant text chunks and grounds the AI's response in exact store policy.

🧠 What I Learned
Handled Spring AI integrations (ChatClient, ChatModel, VectorStore, EmbeddingModel).

Executed local LLM operations and embedding generation with Ollama (llama3.1, nomic-embed-text).

Configured conversation memory via MessageChatMemoryAdvisor.

Integrated dynamic backend Tool Calling (@Tool) with Spring services.

Implemented RAG (Retrieval-Augmented Generation) over local PDF documents.

Built production-ready REST controllers with Pagination and Sorting.

🔮 What's Next?
🔐 Security & JWT Authentication

🧪 Integration testing for Vector Store & RAG pipelines

📦 DTO mappings using MapStruct

🐳 Dockerization & Cloud Deployment

🔗 GitHub Repository: https://github.com/codingwithhitesh/Ai-enabled-BookStore-backend

#Java #SpringBoot #SpringAI #GenerativeAI #RAG #AI #BackendDevelopment #Ollama #VectorDatabase #LLM #ToolCalling #SpringDataJPA #Hibernate #RESTAPI #LearningInPublic
