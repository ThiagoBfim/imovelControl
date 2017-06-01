package br.com.imovelcontrol.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
import br.com.imovelcontrol.repository.Imoveis;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private Imoveis imoveis;

    @GetMapping
    public ModelAndView novo() {
        ModelAndView modelAndView = new ModelAndView("relatorio/RelatorioImovel");
        modelAndView.addObject(new PeriodoRelatorioDTO());
        return modelAndView;
    }

    @GetMapping("/gastosImovel")
    public ModelAndView gerarRelatorio(PeriodoRelatorioDTO periodoRelatorioDTO) {


        Map<String, Object> parametros = new HashMap<>();
        //TODO fazer para pegar a primeira data caso ela seja null.
        Date dataInicio = periodoRelatorioDTO.getDataInicio() != null
                ? Date.from(LocalDateTime.of(periodoRelatorioDTO.getDataInicio(),
                LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant())
                : new Date();
        Date dataFim = periodoRelatorioDTO.getDataFim() != null
                ? Date.from(LocalDateTime.of(periodoRelatorioDTO.getDataFim(),
                LocalTime.of(23, 59, 59)).atZone(ZoneId.systemDefault()).toInstant())
                : new Date();

        List<RelatorioImovelDTO> relatorioImovelDTOs = imoveis.retrieveRelatorioImovelDTO(periodoRelatorioDTO);
          // TODO IMPLEMENTAR UMA LOGICA PARA CASO NÃO TENHA RESULTADO MOSTRAR EM UMA MODAL.
        parametros.put("format", "pdf");
        parametros.put("data_inicio", dataInicio);
        parametros.put("data_fim", dataFim);
        parametros.put("dadosRelatorios", relatorioImovelDTOs);
        parametros.put("datasource", new JREmptyDataSource());
        return new ModelAndView("relatorio_gastos", parametros);
    }

}
