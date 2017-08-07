package br.com.imovelcontrol.service.exception;

/**
 * Created by marcosfellipec on 07/08/17.
 */
public class TelefoneLocatarioInvalidoException extends RuntimeException{
    public TelefoneLocatarioInvalidoException(String message){
        super(message);
    }
}
