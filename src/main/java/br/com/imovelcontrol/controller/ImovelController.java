package br.com.imovelcontrol.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.imovelcontrol.controller.page.PageWrapper;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.enuns.Estado;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.CadastroImovelService;
import br.com.imovelcontrol.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/imovel")
public class ImovelController {

	@Autowired
	private Imoveis imoveis;
	
	@Autowired
	private CadastroImovelService cadastroImovelService;

	@RequestMapping("/novo")
	public ModelAndView novo(Imovel imovel) {
		ModelAndView mAndView = new ModelAndView("imovel/CadastroImovel");
		setAllObjectsFromImovelToModelView(mAndView);

		return mAndView;
	}

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Imovel imovel, BindingResult result, Model model,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			return novo(imovel);
		}

		cadastroImovelService.salvar(imovel);
		attributes.addFlashAttribute("mensagem", "Imovel Salvo com sucesso!");
		return new ModelAndView("redirect:/imovel/novo");
	}

	@GetMapping
	public ModelAndView pesquisar(Imovel imovel, BindingResult result, @PageableDefault(size=5) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView modelAndView = new ModelAndView("imovel/PesquisaImovel");
		setAllObjectsFromImovelToModelView(modelAndView);
		PageWrapper<Imovel> pagina = new PageWrapper<>(imoveis.filtrar(imovel, pageable), httpServletRequest);
		modelAndView.addObject("pagina", pagina);
		return modelAndView;
	}


	private void setAllObjectsFromImovelToModelView(ModelAndView modelAndView) {
		modelAndView.addObject("estados", Estado.values());
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable Long codigo) {
		Imovel imovel = imoveis.findOne(codigo);
		try {
			cadastroImovelService.excluir(imovel);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok().build();
	}
	

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Imovel imovel = imoveis.findOne(codigo);
		ModelAndView mAndView = novo(imovel);
		setAllObjectsFromImovelToModelView(mAndView);
		mAndView.addObject(imovel);
		return mAndView;
	}
	
	
}
