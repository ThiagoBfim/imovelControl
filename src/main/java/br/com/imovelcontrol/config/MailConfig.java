package br.com.imovelcontrol.config;

import java.util.Properties;

import br.com.imovelcontrol.mail.EmailSenderConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

    @Profile("local")
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(env.getProperty("EMAIL_USERNAME"));
        mailSender.setPassword(env.getProperty("EMAIL_PASSWORD"));

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

    @Profile("prod")
    @Bean
    public JavaMailSender mailSenderProd() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.sendgrid.net");
        mailSender.setPort(587);
        mailSender.setUsername(env.getProperty("EMAIL_USERNAME"));
        mailSender.setPassword(env.getProperty("EMAIL_PASSWORD"));

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.debug", false);
        props.put("mail.smtp.connectiontimeout", 10000); //miliseconds

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}
