package com.smeshariks.pms.services;

import java.util.Optional;

public interface ChatRoomService {

    String getChatId(String senderId, String recipientId, boolean createIfNotExist);
}
