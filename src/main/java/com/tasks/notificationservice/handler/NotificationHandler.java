package com.tasks.notificationservice.handler;

import com.tasks.notificationservice.entity.Notification;

public interface NotificationHandler {

    // TODO: 24.10.2018 Use of aspects can avoid incorrect extra_param data injection
    void sendNotification(Notification notification);
}
