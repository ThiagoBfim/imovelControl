package br.com.bomfim.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.bomfim.service.CadastroImovelService;
import br.com.bomfim.storage.FotoStorage;
import br.com.bomfim.storage.local.FotoStorageLocal;

@Configuration
@ComponentScan(basePackageClasses = CadastroImovelService.class)
public class ServiceConfig {

	@Bean
	public FotoStorage fotoStorage(){
		return new FotoStorageLocal();
	}
}
