package com.pmrodrigues.condominio.utilities;

import javax.validation.constraints.Email;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@RequiredArgsConstructor
@Scope(value = "prototype")
public class EmailService {
    private final JavaMailSender emailSender;

    private Collection<String> to = new HashSet<>();
    private String subject;
    private String text;
    private String from;

    public EmailService to(@NonNull @Email String to) {
        this.to.add(to);
        return this;
    }

    public EmailService subject(@NonNull String subject){
        this.subject = subject;
        return this;
    }

    public EmailService text(@NonNull String text){
        this.text = text;
        return this;
    }

    public EmailService from(@NonNull String from) {
        this.from = from;
        return this;
    }

    @SneakyThrows
    public void send() {
        val message = emailSender.createMimeMessage();

        val helper = new MimeMessageHelper(message, UTF_8.toString());
        helper.setFrom(this.from);
        helper.setSubject(this.subject);
        helper.setText(this.text);
        helper.setTo(this.to.toArray(new String[0]));

        emailSender.send(message);
    }

}
