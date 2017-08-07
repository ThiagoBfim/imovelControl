package br.com.imovelcontrol.service.exception;

/**
 * Created by marcosfellipec on 07/08/17.
 */
public class NomeLocatarioInvalidoException extends RuntimeException {
    public NomeLocatarioInvalidoException(String message){
        super(message);
    }
}
