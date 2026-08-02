# 📚 AI-Powered Online Bookstore (Spring Boot + Spring AI + Ollama)

An intelligent Online Bookstore backend built using **Spring Boot** and **Spring AI**. Besides traditional REST APIs for managing users, books, carts, and orders, this project demonstrates how a Large Language Model (LLM) can interact with real application data using **AI Tools (Function Calling)** and **Conversation Memory**.

This project was created as a learning project to understand how modern AI can be integrated into enterprise Java applications without sending the entire database to the model.

---

# ✨ Features

## Core Bookstore

* User Management
* Book Management
* Shopping Cart
* Order Placement
* H2 In-Memory Database
* Spring Data JPA
* Global Exception Handling
* REST APIs

---

## AI Features

### 🤖 General AI Assistant

Users can ask normal AI questions.

Example:

```
What is Artificial Intelligence?
```

---

### 👤 Personalized Customer Assistant

The AI understands the current customer by reading information from the application's database.

Example:

```
Can I afford a laptop costing ₹45,000?
```

Instead of answering generically, the assistant can use the customer's stored information to produce a personalized response.

---

### 🧠 Conversation Memory

This project uses

```
MessageWindowChatMemory
```

to remember previous conversations.

Example:

User:

```
My favourite category is Science.
```

Later...

```
Recommend me another book.
```

The assistant remembers earlier conversation without the user repeating everything.

---

### 🔧 AI Tool Calling (Function Calling)

One of the most interesting features of this project.

Instead of giving the LLM direct database access, selected Java methods are exposed as **AI Tools**.

Current implemented tool:

* Check Book Stock

Example prompt:

```
Is "Atomic Habits" available?
```

The LLM automatically decides that it needs inventory information, invokes the Java tool, receives the result, and then generates a natural-language response.

This keeps database access secure while allowing the AI to retrieve real-time information.

---

# 🏗 Project Architecture

```
                User
                  │
                  ▼
          REST Controller
                  │
                  ▼
      AiChatAssistantService
                  │
       ┌──────────┴──────────┐
       │                     │
       ▼                     ▼
 Spring AI ChatClient     AI Tool
       │              (BookTool.java)
       │                     │
       ▼                     ▼
    Ollama LLM         Book Repository
       │                     │
       └──────────┬──────────┘
                  ▼
            Final AI Response
```

---

# 🛠 Technologies Used

* Java
* Spring Boot
* Spring AI
* Spring Data JPA
* Ollama
* H2 Database
* Maven

---

# 📂 Project Structure

```
bookstore
│
├── AiChatController.java
├── AiChatAssistantService.java
├── Book.java
├── User.java
├── Cart.java
├── Order.java
├── BookTool.java
├── UserBookService.java
├── Repository Classes
├── GlobalExceptionHandler.java
└── BookstoreApplication.java
```

---

# 🚀 Running the Project

## 1. Clone the repository

```
git clone <repository-url>
```

---

## 2. Start Ollama

Run your Ollama server.

Example model:

```
llama3.2
```

---

## 3. Start Spring Boot

Run

```
BookstoreApplication.java
```

---

## 4. Open H2 Database

```
http://localhost:8080/h2-console
```

Database URL

```
jdbc:h2:mem:bookstore
```

---

# Example REST Endpoints

## Add User

```
POST
/api/H2/OnlineBookStore/India/addUser
```

---

## AI General Chat

```
GET

/Jaipur-BookStore-online/ai/GeneralChat?msg=Hello
```

---

## Customer AI Chat

```
GET

/Jaipur-BookStore-online/ai/customer/1/askAi?msg=Can I buy a laptop?
```

---

# Example AI Tool Flow

```
User:
Is Atomic Habits available?

↓

LLM decides it needs inventory information

↓

Calls BookTool.checkBookStock()

↓

BookRepository queries H2 Database

↓

BookTool returns stock information

↓

LLM generates a human-friendly answer
```

---

# Learning Objectives

This project demonstrates:

* Spring Boot REST API development
* Layered Architecture
* Spring Data JPA
* AI integration using Spring AI
* ChatClient
* Conversation Memory
* Function Calling (AI Tools)
* Connecting AI with enterprise applications
* H2 database integration

---

# Future Improvements

* JWT Authentication
* Role-Based Authorization
* DTO Layer
* MySQL/PostgreSQL support
* Docker
* RAG (Retrieval-Augmented Generation)
* Vector Database Integration
* Streaming AI Responses
* React or Angular Frontend
* Chat History Persistence
* Unit Testing
* Integration Testing

---

# Author

**Hitesh S**

Backend Developer | Java | Spring Boot | Spring AI

This project was built to explore how enterprise Java applications can integrate Large Language Models using Spring AI while following clean architecture principles.
