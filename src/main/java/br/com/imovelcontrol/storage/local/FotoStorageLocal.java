//package br.com.imovelcontrol.storage.local;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//import br.com.imovelcontrol.storage.FotoStorage;
//import net.coobird.thumbnailator.Thumbnails;
//import net.coobird.thumbnailator.name.Rename;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import static java.nio.file.FileSystems.getDefault;
//
//@Profile("prod")
//@Component
//public class FotoStorageLocal implements FotoStorage {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
//    private Path local;
//
//    public FotoStorageLocal() {
//        this(getDefault().getPath(System.getProperty("user.home"), ".imovelControlFotos"));
//    }
//
//    public FotoStorageLocal(Path path) {
//        this.local = path;
//        criarPastas();
//    }
//
//    @Override
//    public String salvar(MultipartFile[] files) {
//        String novoNome = null;
//        if (files != null && files.length > 0) {
//            MultipartFile arquivo = files[0];
//            novoNome = renomearArquivo(arquivo.getOriginalFilename());
//            try {
//                arquivo.transferTo(new File(this.local.toAbsolutePath() + getDefault().getSeparator() + novoNome));
//            } catch (IOException e) {
//                throw new RuntimeException("Erro salvando foto", e);
//            }
//        }
//        try {
//            Thumbnails.of(this.local.resolve(novoNome).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
//        } catch (IOException e) {
//            throw new RuntimeException("Erro gerando Thumbnail.");
//        }
//        return novoNome;
//    }
//
//    @Override
//    public byte[] recuperar(String nomeFoto) {
//        try {
//            return Files.readAllBytes(this.local.resolve(nomeFoto));
//        } catch (IOException e) {
//            throw new RuntimeException("Erro lendo o arquivo", e);
//        }
//
//    }
//
//    private void criarPastas() {
//        try {
//            Files.createDirectories(this.local);
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Pasta criada para salvar fotos.");
//                LOGGER.debug("Pasta default:" + this.local.toAbsolutePath());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Erro na hoa de salvar imagem ou foto", e);
//        }
//    }
//
//    @Override
//    public void excluir(String foto) {
//        try {
//            Files.deleteIfExists(this.local.resolve(foto));
//            Files.deleteIfExists(this.local.resolve(THUMBNAIL_PREFIX + foto));
//        } catch (IOException e) {
//            LOGGER.warn("Erro ao apagar foto :", foto + "Mensagem: " + e.getMessage());
//        }
//
//    }
//
//    @Override
//    public String getUrl(String nomeFoto) {
//        return "http://localhost:8080/brewer/fotos/" + nomeFoto;
//    }
//
//
//}
