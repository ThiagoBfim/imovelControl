package br.com.imovelcontrol.model.enuns;

public enum TipoPiso {

	CERAMICA("C", "Ceramica"),
	PORCELANATO("P","Porcelanato"),
	PISSO_GROSSO("G","Piso Grosso");
	
	private String codigo;
	private String descricao;
	
	TipoPiso(String codigo, String descricao) {
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
