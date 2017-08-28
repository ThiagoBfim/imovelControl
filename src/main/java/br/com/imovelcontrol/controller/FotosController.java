package br.com.imovelcontrol.controller;

import br.com.imovelcontrol.dto.FotoDTO;
import br.com.imovelcontrol.storage.FotoStorage;
import br.com.imovelcontrol.storage.FotosStorageRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fotos")
public class FotosController {

    @Autowired
    private FotoStorage fotoStorage;

    @PostMapping
    public DeferredResult<FotoDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
        DeferredResult<FotoDTO> resultado = new DeferredResult<>();
        Thread thread = new Thread(new FotosStorageRunnable(files, resultado, fotoStorage));
        thread.start();
        return resultado;
    }

    @DeleteMapping("/{nameFile}")
    public void delete(@PathVariable String nameFile) {
        fotoStorage.excluir(nameFile);
    }

    @RequestMapping(path = "/{nome:.*}")
    public byte[] recuperar(@PathVariable String nome) {
        return fotoStorage.recuperar(nome);
    }

}
