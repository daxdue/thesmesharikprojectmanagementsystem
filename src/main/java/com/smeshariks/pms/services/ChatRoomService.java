package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.chat.ChatRoom;

import java.util.List;
import java.util.Optional;


public interface ChatRoomService {

    String getChatId(String senderId, String recipientId, boolean createIfNotExist);
    List<ChatRoom> findAllRooms(String senderId);
    List<ChatRoom> findRoomByChatId(String chatId);

}
