package com.tasks.notificationservice.handler;

import com.tasks.notificationservice.entity.Notification;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpHandler implements NotificationHandler {

    private static HttpURLConnection connection;

    @Override
    public void sendNotification(Notification notification) {

        String urlRecipient = notification.getExtra_param();
        String urlMessage = notification.getMessage();

        byte[] postData = urlMessage.getBytes(StandardCharsets.UTF_8);

        try {

            URL url = new URL(urlRecipient);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Simple Solution Java client");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
}
