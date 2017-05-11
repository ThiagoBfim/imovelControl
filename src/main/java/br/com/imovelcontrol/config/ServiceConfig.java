package br.com.imovelcontrol.config;


import br.com.imovelcontrol.service.CadastroImovelService;
import br.com.imovelcontrol.storage.FotoStorage;
import br.com.imovelcontrol.storage.local.FotoStorageLocal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CadastroImovelService.class)
public class ServiceConfig {

	@Bean
	public FotoStorage fotoStorage(){
		return new FotoStorageLocal();
	}
}
