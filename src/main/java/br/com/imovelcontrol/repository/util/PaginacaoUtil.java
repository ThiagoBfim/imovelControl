package br.com.imovelcontrol.repository.util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginacaoUtil {

	public void paginacao(Pageable pageable, Criteria criteria) {
		int totalRegistros = pageable.getPageSize();
		int paginaAtual = pageable.getPageNumber();
		int primeiroRegistro = paginaAtual * totalRegistros;
		
		criteria.setFirstResult(primeiroRegistro);
		criteria.setMaxResults(totalRegistros);
		Sort sort = pageable.getSort();
		
		if (sort != null) {
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
		}
	}

}
