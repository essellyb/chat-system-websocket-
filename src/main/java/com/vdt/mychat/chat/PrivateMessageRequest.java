package com.vdt.mychat.chat;

import lombok.Data;

@Data
public class PrivateMessageRequest {
    private String recipient;
    private String content;
}