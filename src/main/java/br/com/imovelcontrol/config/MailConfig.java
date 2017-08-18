package br.com.imovelcontrol.config;

import java.util.Properties;

import br.com.imovelcontrol.mail.EmailSenderConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
@ComponentScan(basePackageClasses = EmailSenderConfigure.class)
@PropertySource(value = {"classpath:env/mail-prod.properties"}, ignoreResourceNotFound = true)
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(env.getProperty("SENDGRID_USERNAME"));
        mailSender.setPassword(env.getProperty("SENDGRID_PASSWORD"));

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.debug", false);
        props.put("mail.smtp.connectiontimeout", 10000); //miliseconds

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}
