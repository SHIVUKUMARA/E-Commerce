package com.example.E_Commerce.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${spring.mail.password}")
    private String sendGridApiKey;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public void sendEmail(String toEmail, String subject, String message) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", message);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            
            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("Failed to send email: " + response.getBody());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Email sending failed due to a technical error: " + ex.getMessage());
        }
    }
}
