package com.vdt.mychat.chat;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageResponse {
    private String sender;
    private String recipient;
    private String content;
    private String sentAt;
}