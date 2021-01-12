package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.chat.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    void saveMessage(ChatMessage chatMessage);
    ChatMessage findById(String id);
    List<ChatMessage> findByChatId(String chatId);
}
