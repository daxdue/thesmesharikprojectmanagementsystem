package com.smeshariks.pms.entities.chat;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "chat_room")
@Data
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "chat_id")
    private String chatId;
    @Column(name = "sender_id")
    private String senderId;
    @Column(name = "recipient_id")
    private String recipientId;
}
