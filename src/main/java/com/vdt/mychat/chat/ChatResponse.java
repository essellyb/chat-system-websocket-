package com.vdt.mychat.chat;


import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponse {

    private String sender;

    private String content;

    private MessageType type;

    private LocalDateTime sentAt;

}
