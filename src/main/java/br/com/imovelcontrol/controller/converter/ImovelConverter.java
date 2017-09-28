package br.com.imovelcontrol.controller.converter;

import br.com.imovelcontrol.model.Imovel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class ImovelConverter implements Converter<String, Imovel> {

    @Override
    public Imovel convert(String codigo) {

        if (!StringUtils.isEmpty(codigo)) {
            Imovel imovel = new Imovel();
            imovel.setCodigo(Long.valueOf(codigo));
            return imovel;
        }
        return null;
    }

}
