package br.com.imovelcontrol.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @GetMapping
    public ModelAndView novo() {
        ModelAndView modelAndView = new ModelAndView("relatorio/RelatorioImovel");
        modelAndView.addObject(new PeriodoRelatorioDTO());
        return modelAndView;
    }

    @PostMapping("/gastosImovel")
    public ModelAndView gerarRelatorio(PeriodoRelatorioDTO periodoRelatorioDTO) {
        Map<String, Object> parametros = new HashMap<>();
        Date dataInicio = periodoRelatorioDTO.getDataInicio() != null
                ? Date.from(LocalDateTime.of(periodoRelatorioDTO.getDataInicio(),
                LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant())
                : new Date();
        Date dataFim = periodoRelatorioDTO.getDataFim() != null
                ? Date.from(LocalDateTime.of(periodoRelatorioDTO.getDataFim(),
                LocalTime.of(23, 59, 59)).atZone(ZoneId.systemDefault()).toInstant())
                : new Date();

        String nomeImovel = periodoRelatorioDTO.getNomeImovel() != null
                ? periodoRelatorioDTO.getNomeImovel() : "";
        // TODO IMPLEMENTAR UMA LOGICA PARA CASO NÃO TENHA RESULTADO MOSTRAR EM UMA MODAL.

        parametros.put("format", "pdf");
        parametros.put("data_inicio", dataInicio);
        parametros.put("data_fim", dataFim);
        parametros.put("nome_imovel", nomeImovel);
        return new ModelAndView("relatorio_gastos", parametros);
    }
}
