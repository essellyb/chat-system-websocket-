package com.vdt.mychat.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatResponse handleMessage(@Payload ChatRequest request) {
        return chatService.builder(request);
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/messages")
    public ChatResponse handleJoin(@Payload ChatRequest request, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", request.getSender());
        return ChatResponse.builder()
                .sender(request.getSender())
                .messageType(MessageType.JOIN)
                .sentAt(LocalDateTime.now().toString())
                .build();
    }



}