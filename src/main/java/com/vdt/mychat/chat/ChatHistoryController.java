package com.vdt.mychat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatHistoryController {

    private final ChatService chatService;

    @GetMapping("/history")
    public List<ChatMessage> getHistory() {
        return chatService.getHistory();
    }
}
