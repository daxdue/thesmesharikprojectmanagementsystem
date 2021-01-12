package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.chat.ChatRoom;
import com.smeshariks.pms.repositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public String getChatId(String senderId, String recipientId, boolean createIfNotExist) {
        ChatRoom chatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if(chatRoom != null) {
            return chatRoom.getChatId();
        } else {
            if(!createIfNotExist) {
                return null;
            }
            String chatId = String.format("%s_%s", senderId, recipientId);
            ChatRoom senderRecipient = new ChatRoom();
            senderRecipient.setChatId(chatId);
            senderRecipient.setSenderId(senderId);
            senderRecipient.setRecipientId(recipientId);

            ChatRoom recipientSender = new ChatRoom();
            recipientSender.setChatId(chatId);
            recipientSender.setSenderId(recipientId);
            recipientSender.setRecipientId(senderId);

            chatRoomRepository.save(senderRecipient);
            chatRoomRepository.save(recipientSender);
            return chatId;
        }
    }
}
