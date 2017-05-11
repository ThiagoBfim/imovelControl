package br.com.imovelcontrol.storage.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import br.com.imovelcontrol.storage.FotoStorage;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import static java.nio.file.FileSystems.getDefault;

public class FotoStorageLocal implements FotoStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
	private Path local;
	private Path localTemporario;

	public FotoStorageLocal() {
		this(getDefault().getPath(System.getProperty("user.home"), ".bomfimfotos"));
	}

	public FotoStorageLocal(Path path) {
		this.local = path;
		criarPastas();
	}

	@Override
	public String salvarTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		if (files != null && files.length > 0) {
			MultipartFile arquivo = files[0];
			novoNome = renomearArquivo(arquivo.getOriginalFilename());
			try {
				arquivo.transferTo(
						new File(this.localTemporario.toAbsolutePath() + getDefault().getSeparator() + novoNome));
			} catch (IOException e) {
				throw new RuntimeException("Erro salvando foto na pasta temporaria", e);
			}
		}
		return novoNome;
	}
	
	@Override
	public byte[] recuperarFotoTemporaria(String nome) {
		try {
			return Files.readAllBytes(this.localTemporario.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo o arquivo temporario", e);
		}
	}
	
	@Override
	public byte[] recuperar(String nomeFoto) {
		try {
			return Files.readAllBytes(this.local.resolve(nomeFoto));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo o arquivo", e);
		}
		
	}
	
	@Override
	public void apagarFotoTemporaria(String nomeFoto) {
		try {
			Files.deleteIfExists(this.localTemporario.resolve(nomeFoto));
		} catch (Exception e) {
			throw new RuntimeException("Erro ao apagar foto temporária");
		}
		
	}

	@Override
	public void salvar(String foto) {
		try {
			Files.move(this.localTemporario.resolve(foto), this.local.resolve(foto));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao transferir arquivo para destino final.");
		}
		
		try {
			Thumbnails.of(this.local.resolve(foto).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro gerando Thumbnail.");
		}
		
	}

	private void criarPastas() {
		try {
			Files.createDirectories(this.local);
			this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pasta criada para salvar fotos.");
				LOGGER.debug("Pasta default:" + this.local.toAbsolutePath());
				LOGGER.debug("Pasta temporaria:" + this.localTemporario.toAbsolutePath());
			}
		} catch (IOException e) {
			throw new RuntimeException("Erro na hoa de salvar imagem ou foto", e);
		}

	}

	private static String renomearArquivo(String nomeOriginal) {
		String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Nome do arquivo original %s + novo nome: %s ", nomeOriginal, novoNome));
		}
		return novoNome;
	}


}
