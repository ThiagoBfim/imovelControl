package br.com.imovelcontrol.service.exception;

/**
 * Created by marcosfellipec on 22/07/17.
 */
public class LoginUsuarioNaoEncontradoException extends RuntimeException {

    public LoginUsuarioNaoEncontradoException(String message){
        super(message);
    }
}
