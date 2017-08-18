package br.com.imovelcontrol.mail;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Usuario on 17/08/2017.
 */
@Component
public class EmailSenderConfigure {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderConfigure.class);


    private String mensagem;
    private String titulo;
    private String destinarario;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDestinarario() {
        return destinarario;
    }

    public void setDestinarario(String destinarario) {
        this.destinarario = destinarario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Async
    public void enviar() {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(new InternetAddress(env.getProperty("SENDGRID_USERFROM"), "ImovelControl")); //Remetente
            helper.setTo(getDestinarario());
            helper.setSubject(getTitulo());
            helper.setText(getMensagem(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.error("Erro enviando e-mail", e);
        }

    }
}
