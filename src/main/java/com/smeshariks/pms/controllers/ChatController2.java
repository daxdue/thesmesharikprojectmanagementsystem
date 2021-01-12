package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.entities.chat.ChatMessage;
import com.smeshariks.pms.entities.chat.ChatRoom;
import com.smeshariks.pms.services.ChatMessageService;
import com.smeshariks.pms.services.ChatRoomService;
import com.smeshariks.pms.services.UserService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller

public class ChatController2 {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;
    @Autowired private ChatRoomService chatRoomService;
    @Autowired private UserService userService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        String chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId);

        chatMessageService.saveMessage(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId() + "_" + chatMessage.getSenderId(),"/queue/messages",
                chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId() + "_" + chatMessage.getRecipientId(),"/queue/messages",
                chatMessage);
    }

    @GetMapping("/chat/{recipient}")
    public String getChat(@PathVariable String recipient, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setUsername(user.getUsername());
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());
        model.addAttribute("user", smesharikDto);
        model.addAttribute("friend", recipient);
        String chatUser = userService.findUserByUsername(recipient).getName();
        String chatTitle = user.getName() + " (вы) и " + recipient + " (" + chatUser + ")";
        model.addAttribute("chatTitle", chatTitle);

        String chatId = chatRoomService.getChatId(user.getUsername(), recipient, false);
        if(chatId != null) {
            model.addAttribute("messages", chatMessageService.findByChatId(chatId));
        }

        return "chat";
    }

    @GetMapping("/chat/chatroom/{room}")
    public String getChatRoom(@PathVariable String room, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setUsername(user.getUsername());
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        List<ChatRoom> chatRooms = chatRoomService.findRoomByChatId(room);

        model.addAttribute("user", smesharikDto);
        String recipient = "";
        if(smesharikDto.getUsername().equals(chatRooms.get(0).getSenderId())) {
            model.addAttribute("friend", chatRooms.get(0).getRecipientId());
            recipient = chatRooms.get(0).getRecipientId();
        } else {
            model.addAttribute("friend", chatRooms.get(0).getSenderId());
            recipient = chatRooms.get(0).getSenderId();
        }

        String chatTitle = user.getName() + " (вы) и " + recipient + " (" + user.getName() + ")";
        model.addAttribute("chatTitle", chatTitle);

        String chatId = chatRoomService.getChatId(user.getUsername(), recipient, false);
        if(chatId != null) {
            model.addAttribute("messages", chatMessageService.findByChatId(chatId));
        }

        return "chat";
    }

    @GetMapping("/chatrooms")
    public String getChatList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setUsername(user.getUsername());
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        List<ChatRoom> chatRooms = chatRoomService.findAllRooms(smesharikDto.getUsername());

        model.addAttribute("user", smesharikDto);
        model.addAttribute("rooms", chatRooms);

        return "chatList";
    }
}
