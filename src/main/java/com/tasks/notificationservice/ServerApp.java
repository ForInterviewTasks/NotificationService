package com.tasks.notificationservice;

import com.tasks.notificationservice.entity.Notification;
import com.tasks.notificationservice.entity.NotificationType;
import com.tasks.notificationservice.handler.HttpHandler;
import com.tasks.notificationservice.handler.MailHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {

    private static final TreeSet<Notification> notifications = new TreeSet<>();

    private int serverPort;

    private static HttpHandler httpHandler;
    private static MailHandler mailHandler;


    public ServerApp(int serverPort, HttpHandler httpHandler, MailHandler mailHandler) {
        this.serverPort = serverPort;
        setHttpHandler(httpHandler);
        setMailHandler(mailHandler);
    }

    public static HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public static void setHttpHandler(HttpHandler httpHandler) {
        ServerApp.httpHandler = httpHandler;
    }

    public static MailHandler getMailHandler() {
        return mailHandler;
    }

    public static void setMailHandler(MailHandler mailHandler) {
        ServerApp.mailHandler = mailHandler;
    }

    private static class Processor extends Thread {

        @Override
        public void run() {
            try {
                proceed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void proceed() throws InterruptedException {
            while (true) {
                synchronized (notifications) {

                    if (!notifications.isEmpty()) {
                        Notification notification = notifications.first();
                        Date currentTime = new Date();
                        if (notifications.first() != null
                                && notification.getTime().before(currentTime)
                                || notification.getTime().equals(currentTime)) {

                            notifications.remove(notification);
                            NotificationType type = notification.getNotification_type();
                            if (type == NotificationType.HTTP) {
                                httpHandler.sendNotification(notification);
                            }
                            if (type == NotificationType.MAIL) {
                                mailHandler.sendNotification(notification);
                            }
                        }
                    }
                }
            }
        }
    }

    private static class Receiver implements Runnable {

        private final Socket receivingSocket;
        private final ObjectOutputStream outputStream;
        private final ObjectInputStream inputStream;

        public Receiver(Socket receivingSocket) throws IOException {
            this.receivingSocket = receivingSocket;
            this.outputStream = new ObjectOutputStream(receivingSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(receivingSocket.getInputStream());
            getRemoteSocketAddress();
        }

        @Override
        public void run() {

            while (true) {
                try {
                    Notification notification = receive();
                    addNotification(notification);
                } catch (Exception e) {
                    // TODO: 27.10.2018 To learn how to prevent SocketException
//                    e.printStackTrace();
                    break;
                }
            }
        }

        private void addNotification(Notification notification) {
            synchronized (notifications) {
                notifications.add(notification);
            }
        }

        private Notification receive() throws IOException, ClassNotFoundException {
            synchronized (inputStream) {
                return (Notification) inputStream.readObject();
            }
        }

        public SocketAddress getRemoteSocketAddress() {
            return receivingSocket.getRemoteSocketAddress();
        }

    }

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {

            Processor processor = new Processor();
            processor.start();

            while (true) {
                Socket receivingSocket = serverSocket.accept();

                ExecutorService service = Executors.newCachedThreadPool();
                service.submit(new Receiver(receivingSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        HttpHandler httpHandler = new HttpHandler();
        MailHandler mailHandler = new MailHandler(
                "simplesolutiontask@rambler.ru",
                "GwaSAp6hNfWNzwi",
                "smtp.rambler.ru",
                "587");

        new ServerApp(12345, httpHandler, mailHandler).runServer();
    }
}
