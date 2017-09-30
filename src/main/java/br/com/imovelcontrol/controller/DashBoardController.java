package br.com.imovelcontrol.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.dto.AluguelGraficoDTO;
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
    private Imoveis imoveis;

    @Autowired
    private Alugueis alugueis;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @Autowired
    private InformacaoPagamentoService informacaoPagamentoService;

    @Autowired
    private GastosAdicionais gastosAdicionais;



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


    public double valorTotal(Long codigo){
        List<Aluguel> listAlgueis;
        Aluguel aluguel;

        double total = 0.0;

        listAlgueis = alugueis.findByImovel_Codigo(codigo).get();
        for (Aluguel i : listAlgueis) {
            InformacaoPagamento informacaoPagamento = informacaoPagamentoService.retrieveByAluguelAndData(Long.toString(i.getCodigo())).get();

            if (informacaoPagamento.getPago()) {
                total += informacaoPagamento.getValor().doubleValue();
            }
            List<GastoAdicional> gastoAdicionals = gastosAdicionais.findByInformacaoPagamento(informacaoPagamento).get();

            for (GastoAdicional gastoAdicional : gastoAdicionals){
                total -= gastoAdicional.getValorGasto().doubleValue();
            }

        }

        if (total <= 0){
            total = 0;
        }

        DecimalFormat df = new DecimalFormat("0");
        return Double.valueOf(df.format(total));
    }

    @GetMapping("/totalPorMesColuna")
    public @ResponseBody
    List<GraficoColunaImovelDTO> listarTotalVendaPorMesColuna() {

        List<GraficoColunaImovelDTO> graficoColunaImovelDTOS = new ArrayList<>();

        Usuario usuario = usuarioLogadoService.getUsuario();
        List<Imovel> listImovel = imoveis.findByDonoImovelAndExcluido(usuario, false).get();
        listImovel.forEach((Imovel i) -> {
            List<Aluguel> listAlguel = alugueis.findByImovel_Codigo(i.getCodigo()).get();
            listAlguel.forEach((Aluguel aluguel) -> {
                List<InformacaoPagamento> informacaoPagamento = informacaoPagamentoService.findByAluguel(aluguel);

                GraficoColunaImovelDTO graficoColunaImovelDTO = new GraficoColunaImovelDTO();
                graficoColunaImovelDTO.setNome(i.getNome());



            });

        });


        List<Aluguel> aluguels = alugueis.findAll();
        aluguels.forEach(a -> a.getImovel().getDonoImovel().setGrupos(new ArrayList<>()));
        return graficoColunaImovelDTOS;
    }
}
