package br.com.imovelcontrol.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

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
import br.com.imovelcontrol.service.GastoAdicionalService;
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


    private double valorTotal(Long codigo){
        List<Aluguel> listAlgueis;
        Aluguel aluguel;

        double total = 0.0;

        listAlgueis = alugueis.findByImovel_Codigo(codigo).get();
        for (Aluguel i : listAlgueis) {

            if (informacaoPagamentoService.retrieveByAluguelAndData(Long.toString(i.getCodigo())).isPresent()) {
                InformacaoPagamento informacaoPagamento = informacaoPagamentoService.retrieveByAluguelAndData(Long.toString(i.getCodigo())).get();

                if (informacaoPagamento.getPago()) {
                    total += informacaoPagamento.getValor().doubleValue();
                }
                List<GastoAdicional> gastoAdicionals = gastosAdicionais.findByInformacaoPagamento(informacaoPagamento).get();

                for (GastoAdicional gastoAdicional : gastoAdicionals) {
                    //  total -= gastoAdicional.getValorGasto().doubleValue();
                }
            }
        }

        if (total <= 0){
           // total = 1;
        }

        DecimalFormat df = new DecimalFormat("0");
        return Double.valueOf(df.format(total));
    }

    @GetMapping("/totalPorMesColuna")
    public @ResponseBody
    GraficoColunaAgrupadorDTO listarTotalVendaPorMesColuna() {
        List<GraficoColunaImovelDTO> graficoColunaImovelDTOS = imoveis.retrieveGraficoColunaDTO();
        Set<Integer> meses = new LinkedHashSet<>();
        Set<String> listaNomeImoveis = new LinkedHashSet<>();
        List<String> resultadoGrafico = new ArrayList<>();
        if (!CollectionUtils.isEmpty(graficoColunaImovelDTOS)){
            graficoColunaImovelDTOS.forEach(g -> {
                if (meses.isEmpty() || !meses.contains(g.getMes().toString())){
                    resultadoGrafico.add(g.getMes().toString());
                }
                resultadoGrafico.add(g.getValor().toString());
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
