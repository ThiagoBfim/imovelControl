package br.com.imovelcontrol.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import br.com.imovelcontrol.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import br.com.imovelcontrol.thymeleaf.processor.MenuAttributeTagProcessor;
import br.com.imovelcontrol.thymeleaf.processor.MessageElementTagProcessor;
import br.com.imovelcontrol.thymeleaf.processor.OrderElementTagProcessor;
import br.com.imovelcontrol.thymeleaf.processor.PaginationElementTagProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

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
