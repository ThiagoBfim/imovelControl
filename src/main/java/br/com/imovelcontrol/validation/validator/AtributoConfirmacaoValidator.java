package br.com.imovelcontrol.validation.validator;

import java.lang.reflect.InvocationTargetException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import br.com.imovelcontrol.validation.AtributoConfirmacao;
import org.apache.commons.beanutils.BeanUtils;

public class AtributoConfirmacaoValidator implements ConstraintValidator<AtributoConfirmacao, Object> {

	private String atributo;
	private String atributoConfirmacao;

	@Override
	public void initialize(AtributoConfirmacao constraintAnnotation) {
		this.atributo = constraintAnnotation.atributo();
		this.atributoConfirmacao = constraintAnnotation.atributoConfirmacao();
		
	}

	/*
	 * Aula  18.4
	 * */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean valido = false;
		try {
			Object primerioAtributo = BeanUtils.getProperty(value, this.atributo);
			Object confirmacaoAtributo = BeanUtils.getProperty(value, this.atributoConfirmacao);
			
			valido = verificarNullAtributo(primerioAtributo, confirmacaoAtributo) 
					|| ambosSaoIguais(primerioAtributo , confirmacaoAtributo);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException("Erro recuperando atributos", e);
		}
		
		if(!valido){
			context.disableDefaultConstraintViolation();
			String mensagem = context.getDefaultConstraintMessageTemplate();
			ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(mensagem);
			violationBuilder.addPropertyNode(atributoConfirmacao).addConstraintViolation();
		}
		
		return valido;
	}

	private static boolean ambosSaoIguais(Object primerioAtributo, Object confirmacaoAtributo) {
		return primerioAtributo != null && primerioAtributo.equals(confirmacaoAtributo);
	}

	private static boolean verificarNullAtributo(Object primerioAtributo, Object confirmacaoAtributo) {
		return primerioAtributo == null && confirmacaoAtributo == null;
	}

}
