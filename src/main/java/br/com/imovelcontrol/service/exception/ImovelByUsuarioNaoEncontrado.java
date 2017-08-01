package br.com.imovelcontrol.service.exception;

/**
 * Created by marcosfellipec on 28/07/17.
 */
public class ImovelByUsuarioNaoEncontrado extends RuntimeException {
    public ImovelByUsuarioNaoEncontrado(String message){
        super(message);
    }
}
