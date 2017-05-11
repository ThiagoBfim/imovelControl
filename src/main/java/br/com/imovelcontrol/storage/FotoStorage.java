package br.com.imovelcontrol.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {

	public String salvarTemporariamente(MultipartFile[] files);

	public byte[] recuperarFotoTemporaria(String nome);

	public void apagarFotoTemporaria(String nomeFoto);

	public void salvar(String foto);

	public byte[] recuperar(String nomeFoto);
}
