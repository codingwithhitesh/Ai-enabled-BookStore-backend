package com.codewithhitesh.bookstore;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Jaipur-BookStore-online/ai")
public class AiChatController {

        private final AiChatAssistantService aiService;

        public AiChatController(AiChatAssistantService aiService) {

            this.aiService = aiService;
        }


        // Endpoint URL: GET http://localhost:8080/api/ai/chat?msg=YourQuestionHere
        @GetMapping("/GeneralChat")
        public String chatWithAssistant(@RequestParam String msg) {

            return aiService.askGeneralAssistant(msg);
        }


        // Endpoint: http://localhost:8080/api/ai/customer/1/ask?msg=Can I afford a laptop for 45000?
        @GetMapping("/customer/{id}/askAi")
        public String askAboutMyAccount(@PathVariable int id, @RequestParam String msg) {
        return aiService.chatWithCustomerAutoMemory(id, msg);
    }
}

