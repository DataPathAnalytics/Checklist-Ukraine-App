package com.datapath.checklistukraineapp.service.notification;

import com.datapath.checklistukraineapp.util.MessageTemplate;

public interface NotificationService {

    void send(MessageTemplate template);
}
