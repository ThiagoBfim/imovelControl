package br.com.imovelcontrol.service.event.imovel;

import javax.persistence.PostLoad;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.storage.FotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Component
public class ImovelListener {

	@Autowired
	private FotoStorage fotoStorage;
	
	@PostLoad()
	public void imovelSalvo(final Imovel imovel){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        imovel.setUrlFoto(fotoStorage.getUrl(imovel.getFotoOrMock()));
        imovel.setUrlThumbnailFoto(fotoStorage.getUrl(FotoStorage.THUMBNAIL_PREFIX + imovel.getFotoOrMock()));
	}
}
