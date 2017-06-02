package br.com.imovelcontrol.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.imovelcontrol.dto.AluguelGraficoDTO;
import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.Imoveis;
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

    @GetMapping
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject(new PeriodoRelatorioDTO());
        return modelAndView;
    }

    @GetMapping("/totalPorMes")
    public @ResponseBody
    List<AluguelGraficoDTO> listarTotalVendaPorMes() {
        List<AluguelGraficoDTO> aluguelGraficoDTOList = new ArrayList<>();
        List<Imovel> listImovel = imoveis.findAll();
        listImovel.forEach(i -> {
            AluguelGraficoDTO aluguelGraficoDTO = new AluguelGraficoDTO();
            aluguelGraficoDTO.setNomeImovel(i.getNome());
            aluguelGraficoDTOList.add(aluguelGraficoDTO);
        });

        return aluguelGraficoDTOList;
    }

    @GetMapping("/totalPorMesColuna")
    public @ResponseBody
    List<Aluguel> listarTotalVendaPorMesColuna() {
        return alugueis.findAll();
    }
}
