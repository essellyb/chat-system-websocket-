package com.vdt.mychat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatResponse builder(ChatRequest request) {
        ChatMessage message = ChatMessage.builder()
                .sender(request.getSender())
                .content(request.getContent())
                .messageType(MessageType.CHAT)
                .sentAt(LocalDateTime.now())
                .build();

        chatRepository.save(message);

        return ChatResponse.builder()
                .sender(message.getSender())
                .content(message.getContent())
                .messageType(MessageType.CHAT)
                .sentAt(message.getSentAt().toString())
                .build();
    }

    public List<ChatMessage> getHistory() {
        return chatRepository.findAll()
                .stream()
                .filter(msg -> msg.getMessageType() == MessageType.CHAT)
                .toList();
    }
}