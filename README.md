🚀 **AI-Powered Bookstore Backend with Customer Chat, Memory & Tool Calling**

Excited to share an upgrade to my Spring Boot Online Bookstore project!

What started as a traditional e-commerce backend gradually became an experiment in combining **Spring Boot + Spring AI + Ollama**.

The idea I wanted to explore was:

**How can an AI assistant interact with the actual backend and database instead of simply generating text?**

### 🏗️ Core Backend

Built with:

🔹 Java & Spring Boot
🔹 Spring Data JPA / Hibernate
🔹 H2 Database
🔹 REST APIs
🔹 User & Book management
🔹 Cart & Order processing
🔹 Pagination & Global Exception Handling

### 🤖 My main feature: TWO types of AI conversations

I implemented two different chat experiences.

**1️⃣ General Bookstore Chat**

A customer can ask general questions such as:

👉 “What are your working hours?”
👉 “Where is the bookstore located?”

This is a general AI assistant with access to bookstore-specific tools.

**2️⃣ Customer-Specific Chat**

This is where I wanted to make the project more interesting.

A customer can chat using their **customer ID**, and the AI receives information about that specific customer from the backend, including:

👤 Name
📊 Customer status
🛒 Total orders

The conversation also maintains **memory using Spring AI's MessageWindowChatMemory**, allowing multi-turn conversations to continue with context.

So the architecture becomes:

**Customer → Spring Boot → AI → Customer Context + Conversation Memory → Response**

### 🔧 AI Tool Calling

I also implemented backend tools using Spring AI's `@Tool`.

For example, if a customer asks:

> “Is Java Complete Reference available?”

The AI can decide that it needs real information and call:

**AI → BookTool → UserBookService → Database → Result → AI → Customer**

I currently have tools for:

📚 Checking book availability
💰 Checking book price

This was one of the biggest learning points for me:

**The AI isn't guessing the database information — it can use backend functionality to retrieve it.**

### 🧠 What I learned

Through this project I got hands-on experience with:

• Spring AI
• ChatClient & ChatModel
• Ollama / local LLMs
• Conversation memory
• AI Tool Calling
• Connecting LLMs with existing Java services
• Building AI features on top of a REST backend

This project is still evolving. Next, I want to explore **RAG, authentication, testing, DTOs and deployment**.

For me, this project represents a shift from simply learning Spring Boot to understanding how **AI capabilities can be integrated into a real backend application.**

🔗 **GitHub:** https://github.com/codingwithhitesh/Ai-enabled-BookStore-backend

#Java #SpringBoot #SpringAI #GenerativeAI #AI #BackendDevelopment #Ollama #LLM #ToolCalling #SpringDataJPA #Hibernate #RESTAPI #LearningInPublic
