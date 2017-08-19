package br.com.imovelcontrol.storage;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {

    String THUMBNAIL_PREFIX = "thumbnail.";

    String salvar(MultipartFile[] files);

    byte[] recuperar(String foto);


    void excluir(String foto);

    String getUrl(String foto);

    default String renomearArquivo(String nomeOriginal) {
        return UUID.randomUUID().toString() + "_" + nomeOriginal;
    }

    default byte[] recuperarThumbnail(String foto) {
        return recuperar(FotoStorage.THUMBNAIL_PREFIX + foto);
    }

}
