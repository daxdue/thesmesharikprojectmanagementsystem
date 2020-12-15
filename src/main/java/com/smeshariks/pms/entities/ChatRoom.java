package com.smeshariks.pms.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chat_room")
@Data
@NoArgsConstructor
public class ChatRoom {

    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
