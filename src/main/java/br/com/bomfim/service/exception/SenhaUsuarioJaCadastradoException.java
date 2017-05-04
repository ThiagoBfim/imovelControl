package br.com.bomfim.service.exception;

@SuppressWarnings("serial")
public class SenhaUsuarioJaCadastradoException extends RuntimeException {

	public SenhaUsuarioJaCadastradoException(String message){
		super(message);
	}
}
