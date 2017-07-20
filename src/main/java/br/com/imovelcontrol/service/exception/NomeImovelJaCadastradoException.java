package br.com.imovelcontrol.service.exception;

/**
 * Created by marcosfellipec on 20/07/17.
 */
public class NomeImovelJaCadastradoException extends RuntimeException {

    public NomeImovelJaCadastradoException(String message){
        super(message);
    }

}
