package br.com.bomfim.model.tipoimovel.enuns;

public enum TipoForro {

	PVC("P", "Pvc"),
	LAJE("L","Laje"),
	GESSO("G","Gesso");
	
	private String codigo;
	private String descricao;
	
	TipoForro(String codigo, String descricao) {
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
