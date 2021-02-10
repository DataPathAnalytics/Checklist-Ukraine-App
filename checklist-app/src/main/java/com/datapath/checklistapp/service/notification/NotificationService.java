package com.datapath.checklistapp.service.notification;

import com.datapath.checklistapp.util.MessageTemplate;

public interface NotificationService {

    void send(MessageTemplate template);
}
