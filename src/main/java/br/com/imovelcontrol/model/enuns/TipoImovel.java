package br.com.imovelcontrol.model.enuns;

public enum TipoImovel {

	KIT("KT","Kit"),
	CASA("CS","Casa"),
	APARTAMENTO("AP","Apartamento"),
	LOJA("LJ", "Loja"),
	SALAO("SA", "Salão");
	
	private String codigo;
	private String descricao;
	
	TipoImovel(String codigo, String descricao) {
		this.setDescricao(descricao);
		this.setCodigo(codigo);
	}

	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
}
