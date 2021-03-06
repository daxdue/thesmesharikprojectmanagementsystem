package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    ChatRoom findBySenderIdAndRecipientId(String senderId, String recipientId);
    List<ChatRoom> findByChatId(String chatId);
    List<ChatRoom> findAllBySenderId(String senderId);
}
