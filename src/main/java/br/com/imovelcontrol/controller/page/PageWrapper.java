package br.com.imovelcontrol.controller.page;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {

	private Page<T> page;
	private UriComponentsBuilder uriComponentsBuilder;

	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		String httpUrl = httpServletRequest.getRequestURL()
				.append(httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString().replaceAll("\\+", "%20").replaceAll("excluido", "");
		this.uriComponentsBuilder = ServletUriComponentsBuilder.fromHttpUrl(httpUrl);
	}

	public List<T> getConteudo() {
		return page.getContent();
	}

	public boolean isVazia() {
		return page.getContent().isEmpty();
	}

	public int getAtual() {
		return page.getNumber();
	}

	public boolean isPrimeira() {
		return page.isFirst();
	}

	public boolean isUltima() {
		return page.isLast();
	}

	public int getTotal() {
		return page.getTotalPages();
	}

	public String urlParaPagina(int pagina) {
		return uriComponentsBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}

	public String urlOrdenada(String field) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
				.fromUriString(uriComponentsBuilder.build(true).encode().toUriString());

		String valorSort = String.format("%s,%s", field, inverterDirecao(field));

		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
	}

	public String inverterDirecao(String propriedade) {
		String direcao = "asc";

		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			direcao = order.isAscending() ? "desc" : "asc";
		}

		return direcao;
	}

	public boolean descendente(String propriedade) {
		return inverterDirecao(propriedade).equals("asc");
	}

	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		return order != null;
	}

}
