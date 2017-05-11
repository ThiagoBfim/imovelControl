package br.com.imovelcontrol.model.enuns;

public enum Estado {

	AC("AC", "Acre"),
	DF("DF","Distrito Federal"),
	GO("GO","Goiais");

	private String codigo;
	private String descricao;
	
	Estado(String codigo, String descricao) {
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
