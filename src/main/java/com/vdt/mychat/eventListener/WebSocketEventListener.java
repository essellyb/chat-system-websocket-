package com.vdt.mychat.eventListener;

import com.vdt.mychat.chat.ChatResponse;
import com.vdt.mychat.chat.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) accessor.getSessionAttributes().get("username");

        if (username != null) {
            messagingTemplate.convertAndSend("/topic/messages", ChatResponse.builder()
                    .sender(username)
                    .messageType(MessageType.LEAVE)
                    .sentAt(LocalDateTime.now().toString())
                    .build());
        }
    }
}
