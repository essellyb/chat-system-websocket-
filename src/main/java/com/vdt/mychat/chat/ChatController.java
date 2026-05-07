package com.vdt.mychat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatResponse sendMessage(@Payload ChatRequest request) {
        return ChatResponse
                .builder()
                .sender(request.getSender())
                .content(request.getContent())
                .sentAt(LocalDateTime.now())
                .build();
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/messages")
    public ChatResponse joinChat(@Payload ChatRequest request,
                                 SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes()
                .put("username", request.getSender());
        return ChatResponse.builder()
                .sender(request.getSender())
                .type(MessageType.JOIN)
                .sentAt(LocalDateTime.now())
                .build();
    }
}
