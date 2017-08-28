package br.com.imovelcontrol.config;


import br.com.imovelcontrol.service.CadastroImovelService;
import br.com.imovelcontrol.storage.FotoStorage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {CadastroImovelService.class, FotoStorage.class})
public class ServiceConfig {

//    @Bean
//    public FotoStorage fotoStorage() {
//        return new FotoStorageS3();
//    }

//    @Bean
//    public FotoStorage fotoStorage() {
//        return new FotoStorageLocal();
//    }
}
