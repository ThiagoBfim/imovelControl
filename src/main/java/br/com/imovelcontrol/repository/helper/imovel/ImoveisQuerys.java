package br.com.imovelcontrol.repository.helper.imovel;

import java.util.Date;
import java.util.List;

import br.com.imovelcontrol.dto.GraficoColunaImovelDTO;
import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioDetalhadoImovelDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImoveisQuerys {

    Page<Imovel> filtrar(Imovel filtro, Usuario usuario, Pageable pageable);

    List retrieveRelatorioImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO);

    Date retrieveMinDataMensalPagamento();

    List<RelatorioDetalhadoImovelDTO> retrieveRelatorioDetalhadoImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO);

    List<GraficoColunaImovelDTO> retrieveGraficoColunaDTO();
}
