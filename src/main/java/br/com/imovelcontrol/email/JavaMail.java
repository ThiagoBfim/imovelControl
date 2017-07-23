package br.com.imovelcontrol.email;

/**
 * Created by marcosfellipec on 21/07/17.
 */
import br.com.imovelcontrol.model.Usuario;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMail {
    private static final String LOGIN = "imovelcontrol@gmail.com";
    private static final String PASSWORD = "imovelcontroltcc123";

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

    private Properties propriedade(){
        Properties props = new Properties();
        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        return  props;
    }

    private Authenticator autenticacao(){

        return new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(LOGIN, PASSWORD);
            }
        };
    }

    private Session sessao(){
        Session session = Session.getDefaultInstance(propriedade(), autenticacao());
        /** Ativa Debug para sessão */
        session.setDebug(true);
        return session;

    }

    public void enviarEmail() {
        try {
            Message message = new MimeMessage(sessao());
            message.setFrom(new InternetAddress(LOGIN)); //Remetente

            Address[] toUser = InternetAddress.parse(getDestinarario());//Destinatário(s)
            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(getTitulo());//Assunto
            message.setText(getMensagem());

            Transport.send(message);//Método para enviar a mensagem criada

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}


