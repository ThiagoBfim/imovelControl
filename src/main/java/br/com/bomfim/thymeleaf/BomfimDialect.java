package br.com.bomfim.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import br.com.bomfim.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import br.com.bomfim.thymeleaf.processor.MenuAttributeTagProcessor;
import br.com.bomfim.thymeleaf.processor.MessageElementTagProcessor;
import br.com.bomfim.thymeleaf.processor.OrderElementTagProcessor;
import br.com.bomfim.thymeleaf.processor.PaginationElementTagProcessor;

public class BomfimDialect extends AbstractProcessorDialect {

	public BomfimDialect() {
		super("Bomfim Imoveis", "bomfim", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processadores = new HashSet<>();
		processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processadores.add(new MessageElementTagProcessor(dialectPrefix));
		processadores.add(new OrderElementTagProcessor(dialectPrefix));
		processadores.add(new PaginationElementTagProcessor(dialectPrefix));
		processadores.add(new MenuAttributeTagProcessor(dialectPrefix));
		return processadores;
	} 

}
