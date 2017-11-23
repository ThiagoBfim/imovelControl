package br.com.imovelcontrol.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.imovelcontrol.dto.AluguelGraficoDTO;
import br.com.imovelcontrol.dto.GraficoColunaAgrupadorDTO;
import br.com.imovelcontrol.dto.GraficoColunaImovelDTO;
import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.GastoAdicional;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.GastosAdicionais;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.InformacaoPagamentoService;
import br.com.imovelcontrol.service.UsuarioLogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Usuario on 21/05/2017.
 */
@Controller
@RequestMapping("/")
public class DashBoardController {

    @Autowired
    private Alugueis alugueis;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @Autowired
    private InformacaoPagamentoService informacaoPagamentoService;

    @Autowired
    private GastosAdicionais gastosAdicionais;

    @Autowired
    private Imoveis imoveis;

    @GetMapping("/dashboard")
    public ModelAndView dashboard() {

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject(new PeriodoRelatorioDTO());

        return modelAndView;
    }

    @GetMapping("/totalPorMes")
    public @ResponseBody
    List<AluguelGraficoDTO> listarTotalVendaPorMes() {

        Usuario usuario = usuarioLogadoService.getUsuario();

        List<AluguelGraficoDTO> aluguelGraficoDTOList = new ArrayList<>();
        Optional<List<Imovel>> listImovel = imoveis.findByDonoImovelAndExcluido(usuario, false);

        listImovel.ifPresent(imovels -> imovels.forEach((Imovel i) -> {

            AluguelGraficoDTO aluguelGraficoDTO = new AluguelGraficoDTO();
            aluguelGraficoDTO.setNomeImovel(i.getNome());
            aluguelGraficoDTO.setRendimento(valorTotal(i.getCodigo()));
            aluguelGraficoDTOList.add(aluguelGraficoDTO);

        }));

        return aluguelGraficoDTOList;
    }


    private double valorTotal(Long codigo) {
        double total = 0;
        Optional<List<Aluguel>> listAlgueis = alugueis.findByImovel_Codigo(codigo);
        if (listAlgueis.isPresent()) {
            for (Aluguel i : listAlgueis.get()) {
                Optional<List<InformacaoPagamento>> informacaoPagamento = informacaoPagamentoService
                        .retrieveInforcamacoesPagamentoByAluguel(Long.toString(i.getCodigo()));

                if (informacaoPagamento.isPresent()) {
                    for (InformacaoPagamento pagamento : informacaoPagamento.get()) {
                        if (pagamento.getPago()) {
                            total += pagamento.getValor().doubleValue();
                        }
                        if (pagamento.getMulta() != null) {
                            total += pagamento.getMulta().doubleValue();
                        }
                        Optional<List<GastoAdicional>> gastoAdicionals = gastosAdicionais
                                .findByInformacaoPagamento(pagamento);
                        if (gastoAdicionals.isPresent()) {
                            for (GastoAdicional gastoAdicional : gastoAdicionals.get()) {
                                total -= gastoAdicional.getValorGasto().doubleValue();
                            }
                        }
                    }

                }
            }

            if (total < 0) {
                total = 0;
            }
        }

        return total;
    }

    @GetMapping("/totalPorMesColuna")
    public @ResponseBody
    GraficoColunaAgrupadorDTO listarTotalVendaPorMesColuna() {

        List<GraficoColunaImovelDTO> graficoColunaImovelDTOS = imoveis.retrieveGraficoColunaDTO();
        Set<Integer> meses = new LinkedHashSet<>();
        Set<String> listaNomeImoveis = new LinkedHashSet<>();

        if (!CollectionUtils.isEmpty(graficoColunaImovelDTOS)) {

            graficoColunaImovelDTOS.forEach(g -> {

                meses.add(g.getMes());
                listaNomeImoveis.add(g.getNome());

            });
        }

        GraficoColunaAgrupadorDTO agrupadorDTO = new GraficoColunaAgrupadorDTO();
        agrupadorDTO.setMeses(meses);
        agrupadorDTO.setListaNomeImoveis(listaNomeImoveis);
        agrupadorDTO.setValores(graficoColunaImovelDTOS);

        return agrupadorDTO;

    }


}
