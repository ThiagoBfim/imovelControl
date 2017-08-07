package br.com.imovelcontrol.service.exception;

/**
 * Created by marcosfellipec on 07/08/17.
 */
public class CpfLocatarioInvalidoException extends RuntimeException {
    public CpfLocatarioInvalidoException(String message){
        super(message);
    }
}
