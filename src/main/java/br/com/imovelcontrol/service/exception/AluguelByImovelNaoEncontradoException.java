package br.com.imovelcontrol.service.exception;

import org.omg.SendingContext.RunTime;

/**
 * Created by marcosfellipec on 28/07/17.
 */
public class AluguelByImovelNaoEncontradoException extends RuntimeException {

    public AluguelByImovelNaoEncontradoException(String message){
        super(message);
    }

}
