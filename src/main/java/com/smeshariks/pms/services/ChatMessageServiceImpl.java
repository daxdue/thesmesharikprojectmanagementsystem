package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.chat.ChatMessage;
import com.smeshariks.pms.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public ChatMessage findById(String id) {
        return chatMessageRepository.findById(id).orElse(new ChatMessage());
    }

    public List<ChatMessage> findByChatId(String chatId) {
        return chatMessageRepository.findAllByChatId(chatId);
    }
}
