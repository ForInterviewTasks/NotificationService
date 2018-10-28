package com.tasks.notificationservice;

import com.tasks.notificationservice.entity.Notification;
import com.tasks.notificationservice.entity.NotificationType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class Client {

    private static class ClientThread implements Runnable {

        @Override
        public void run() {

        }

        private void send(Notification notification) {
            try {
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream outputStream =
                        new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeObject(notification);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        ClientThread clientThread = new ClientThread();
        for (int i = 0; i < 10; i++) {
            Notification notification = new Notification(
                    "" + i,
                    "New Message" + i,
                    new Date(),
                    NotificationType.MAIL,
                    "simplesolutiontask@rambler.ru"); //recipient

            clientThread.send(notification);
        }
    }
}
