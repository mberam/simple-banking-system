package com.leapwise.banking.service;

import com.leapwise.banking.db.Account;
import com.leapwise.banking.db.Transaction;
import com.leapwise.banking.exceptions.BankingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@AllArgsConstructor
@Log4j2
public class EmailService {

    JavaMailSender javaMailSender;

    public MimeMessage buildMessage(Transaction transaction, Account account, boolean isSuccess, BigDecimal oldBalance, boolean isSender) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, UTF_8.name());
            mimeMessageHelper.setFrom("mihovilleapwise@gmail.com");
            mimeMessageHelper.setTo(account.getCustomer().getEmail());

            mimeMessageHelper.setSubject("Transaction Notification");

            String emailBody = createTransactionEmailBody(transaction, account, isSuccess, oldBalance, isSender);
            mimeMessage.setText(emailBody);
        } catch (MessagingException e) {
            throw new BankingException("Email building email message", "Email building email message.", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return mimeMessage;
    }

    public void send(MimeMessage mimeMessage) {
        try {
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new BankingException("Email was not sent correctly", "Email was not sent correctly.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private String createTransactionEmailBody(Transaction transaction, Account account, boolean isSuccess, BigDecimal oldBalance, boolean isSender) {
        String status = isSuccess ? "successfully" : "unsuccessfully";
        String action = isSender ? "taken" : "added";

        return String.format("Hello!\n\n" +
                "The transaction with ID: %s has been processed %s, and the balance: %s has been %s from your account.\n\n" +
                "Old balance: %s\n" +
                "New balance: %s\n\n" +
                "Regards,\n" +
                "Your XYZ bank", transaction.getId(), status, transaction.getAmount(), action, oldBalance, account.getBalance());
    }
}
