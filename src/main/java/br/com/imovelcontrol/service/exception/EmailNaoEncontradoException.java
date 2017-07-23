package br.com.imovelcontrol.service.exception;

/**
 * Created by marcosfellipec on 21/07/17.
 */
public class EmailNaoEncontradoException extends RuntimeException {

    public EmailNaoEncontradoException(String message){
        super(message);
    }
}
