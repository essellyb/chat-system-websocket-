package com.vdt.mychat.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatResponse handleMessage(@Payload ChatRequest request, Principal principal) {
        return chatService.saveAndBuildResponse(request, principal.getName());
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/messages")
    public ChatResponse handleJoin(@Payload ChatRequest request,
                                   SimpMessageHeaderAccessor headerAccessor,
                                   Principal principal) {
        String name = principal.getName();
        headerAccessor.getSessionAttributes().put("username", name);
        return ChatResponse.builder()
                .sender(name)
                .messageType(MessageType.JOIN)
                .sentAt(LocalDateTime.now().toString())
                .build();
    }

    @MessageMapping("/private")
    public void handlePrivateMessage(@Payload PrivateMessageRequest request, Principal principal) {
        String sender = principal.getName();

        PrivateMessageResponse response = PrivateMessageResponse.builder()
                .sender(sender)
                .recipient(request.getRecipient())
                .content(request.getContent())
                .sentAt(LocalDateTime.now().toString())
                .build();

        messagingTemplate.convertAndSendToUser(request.getRecipient(), "/queue/private", response);
        if (!sender.equals(request.getRecipient())) {
            messagingTemplate.convertAndSendToUser(sender, "/queue/private", response);
        }
    }
}