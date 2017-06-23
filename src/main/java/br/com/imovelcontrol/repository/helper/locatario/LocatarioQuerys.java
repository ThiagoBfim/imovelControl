package br.com.imovelcontrol.repository.helper.locatario;

import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
import br.com.imovelcontrol.model.Locatario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocatarioQuerys {

    Page<Locatario> filtrar(Locatario filtro, Pageable pageable);

    //List<RelatorioImovelDTO> retrieveRelatorioImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO);

}
