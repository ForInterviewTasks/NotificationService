package com.tasks.notificationservice.handler;

import com.tasks.notificationservice.entity.Notification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailHandler implements NotificationHandler{

    private String handlerMailUsername;
    private String password;
    private String host;
    private String port;

    public MailHandler(String handlerMailUsername, String password, String host, String port) {
        this.handlerMailUsername = handlerMailUsername;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(handlerMailUsername, password);
                    }
                });

        return session;
    }

    @Override
    public void sendNotification(Notification notification) {

        String mailRecipient = notification.getExtra_param();
        String mailMessage = notification.getMessage();

        try {

            Message message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(handlerMailUsername));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailRecipient));
            message.setSubject("Notification");
            message.setText(mailMessage);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
