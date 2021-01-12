package com.smeshariks.pms.entities.chat;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "chat_id")
    private String chatId;
    @Column(name = "sender_id")
    private String senderId;
    @Column(name = "recipient_id")
    private String recipientId;
    private String content;
    private Date timestamp;


    public String getAvatarColor() {
        String[] colors = {
                "#2196F3", "#32c787", "#00BCD4", "#ff5652", "#ffc107", "#ff85af", "#FF9800", "#39bbb0"
        };

        int hash = 0;
        for(int i = 0; i < senderId.length(); i++) {
            hash = 31 * hash + (int)senderId.charAt(i);
        }
        int indx = Math.abs(hash % colors.length);

        return /*colors[indx];*/"background-color: " + colors[indx] + ";";
    }

    public String getAvatarText() {
        return String.valueOf(senderId.charAt(0));
    }
}
