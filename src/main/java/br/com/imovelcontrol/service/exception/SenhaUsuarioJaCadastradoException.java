package br.com.imovelcontrol.service.exception;

@SuppressWarnings("serial")
public class SenhaUsuarioJaCadastradoException extends RuntimeException {

	public SenhaUsuarioJaCadastradoException(String message){
		super(message);
	}
}
