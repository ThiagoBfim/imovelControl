package br.com.imovelcontrol.service.exception;

@SuppressWarnings("serial")
public class SenhaObrigatoriaException extends RuntimeException {

	public SenhaObrigatoriaException(String message){
		super(message);
	}
}
