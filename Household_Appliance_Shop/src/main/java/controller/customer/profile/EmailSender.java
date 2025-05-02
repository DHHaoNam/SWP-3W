/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.customer.profile;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailSender {

    // Cấu hình email tĩnh để test nhanh
    private static final String HOST = "smtp.gmail.com";
    private static final String FROM_EMAIL = "duytkce181326@fpt.edu.vn";
    private static final String PASSWORD = "ubov cezd ajdw xynh";

    private static final Logger LOGGER = Logger.getLogger(EmailSender.class.getName());

    /**
     * Gửi email xác thực với subject và body cụ thể.
     *
     * @param toEmail địa chỉ người nhận
     * @param subject tiêu đề email
     * @param body nội dung email
     * @throws MessagingException nếu gửi email thất bại
     */
    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        // Cấu hình thuộc tính SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Tạo phiên gửi email có xác thực
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            // Tạo email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body); // Nội dung thuần văn bản

            // Gửi email
            Transport.send(message);
            LOGGER.info("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Failed to send email to " + toEmail, e);
            throw e;
        }
    }
}
