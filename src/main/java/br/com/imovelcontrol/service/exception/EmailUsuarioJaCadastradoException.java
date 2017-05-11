package br.com.imovelcontrol.service.exception;

@SuppressWarnings("serial")
public class EmailUsuarioJaCadastradoException extends RuntimeException {

	public EmailUsuarioJaCadastradoException(String message) {
		super(message);
	}
}
