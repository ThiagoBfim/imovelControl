package br.com.imovelcontrol.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.imovelcontrol.controller.converter.FormatUtil;
import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioDetalhadoImovelDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.UsuarioLogadoService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private Imoveis imoveis;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @RequestMapping("/geral")
    public ModelAndView novo(PeriodoRelatorioDTO periodoRelatorioDTO) {
        return new ModelAndView("relatorio/RelatorioImovel");
    }

    @RequestMapping("/detalhado")
    public ModelAndView novoDetalahdo(PeriodoRelatorioDTO periodoRelatorioDTO) {
        ModelAndView modelAndView = new ModelAndView("relatorio/RelatorioDetalhadoImovel");
        List<Imovel> imoveisList = imoveis.findByDonoImovel_Codigo(usuarioLogadoService
                .getUsuario().getCodigo());
        imoveisList.forEach(i -> {
            if(i.getExcluido()){
                i.setNome( i.getNome() + " - [Excluido]" );
            }
        });
        modelAndView.addObject("imoveis", imoveisList);
        return modelAndView;
    }

    @PostMapping("/gastosImovel")
    public ModelAndView gerarRelatorio(PeriodoRelatorioDTO periodoRelatorioDTO, BindingResult result) {

        Map<String, Object> parametros = createParametersToReport(periodoRelatorioDTO);


        List<RelatorioImovelDTO> relatorioImovelDTOs = imoveis.retrieveRelatorioImovelDTO(periodoRelatorioDTO);
        if (CollectionUtils.isEmpty(relatorioImovelDTOs)) {
            result.rejectValue("dataInicio", "Nenhum Resultado encontrado", "Nenhum Resultado encontrado");
            return novo(periodoRelatorioDTO);
        }
        parametros.put("dadosRelatorios", relatorioImovelDTOs);
        return new ModelAndView("relatorio_gastos", parametros);
    }

    @PostMapping("/gastosDetalhadoImovel")
    public ModelAndView gerarRelatorioDetalhado(PeriodoRelatorioDTO periodoRelatorioDTO, BindingResult result) {
        periodoRelatorioDTO.setMostrarExcluidos(Boolean.TRUE);
        if (periodoRelatorioDTO.getImovel() != null) {
            periodoRelatorioDTO.setImovel(imoveis.findOne(periodoRelatorioDTO.getImovel().getCodigo()));
            Map<String, Object> parametros = createParametersToReport(periodoRelatorioDTO);
            periodoRelatorioDTO.setMostrarExcluidos(periodoRelatorioDTO.getImovel().getExcluido());
            parametros.put("nomeImovel", FormatUtil
                    .inserirCepMascara(periodoRelatorioDTO.getImovel().getNome()
                            + " - " + periodoRelatorioDTO.getImovel().getEndereco().getCep()));

            List<RelatorioDetalhadoImovelDTO> relatorioImovelDTOs = imoveis
                    .retrieveRelatorioDetalhadoImovelDTO(periodoRelatorioDTO).stream().distinct().collect(Collectors.toList());
            relatorioImovelDTOs.removeIf(s -> CollectionUtils.isEmpty(s.getSubRelatorioDetalhadoImovelDTOS()));
            if (CollectionUtils.isEmpty(relatorioImovelDTOs)) {
                result.rejectValue("imovel", "Nenhum Resultado encontrado", "Nenhum Resultado encontrado");
                return novoDetalahdo(periodoRelatorioDTO);
            }
            parametros.put("subReportGastos", "relatorios/relatorio_subReportDetalhado_gastos.jasper");
            parametros.put("subReportGastosAdicionais", "relatorios/relatorio_subReportInfoPagamento_gastos.jasper");
            parametros.put("dadosRelatorios", relatorioImovelDTOs);
            return new ModelAndView("relatorio_detalhado_gastos", parametros);
        } else {
            result.rejectValue("imovel", "Selecione pelo menos um imovel.", "Selecione pelo menos um imovel");
            return novoDetalahdo(periodoRelatorioDTO);
        }

    }

    private Map<String, Object> createParametersToReport(PeriodoRelatorioDTO periodoRelatorioDTO) {
        Date dataInicio;
        if (periodoRelatorioDTO.getDataInicio() != null) {
            dataInicio = Date.from(LocalDateTime.of(periodoRelatorioDTO.getDataInicio(),
                    LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant());
        } else {
            dataInicio = imoveis.retrieveMinDataMensalPagamento();
            if (dataInicio != null) {
                periodoRelatorioDTO.setDataInicio(dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        }
        Date dataFim;
        if (periodoRelatorioDTO.getDataFim() != null) {
            dataFim = Date.from(LocalDateTime.of(periodoRelatorioDTO.getDataFim(),
                    LocalTime.of(23, 59, 59)).atZone(ZoneId.systemDefault()).toInstant());
        } else {
            dataFim = new Date();
            periodoRelatorioDTO.setDataFim(LocalDate.now());
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("format", "pdf");
        parametros.put("data_inicio", dataInicio);
        parametros.put("data_fim", dataFim);
        parametros.put("usuario", usuarioLogadoService.getUsuario().getNome());
        parametros.put("datasource", new JREmptyDataSource());
        return parametros;
    }

}
