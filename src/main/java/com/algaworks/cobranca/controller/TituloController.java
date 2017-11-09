package com.algaworks.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.filter.TituloFilter;
import com.algaworks.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
	private CadastroTituloService cadastroTituloService;

	@RequestMapping("/novo")
	public ModelAndView newRecord() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo()); // Utilizado para o objeto informado no formulario para validação do bean
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@Validated Titulo titulo, Errors erros, RedirectAttributes attributes) {
		// TODO: save the object in database

		if (erros.hasErrors()) {
			return CADASTRO_VIEW;
		}

		try {
			cadastroTituloService.save(titulo);
			attributes.addFlashAttribute("message", "Título salvo com sucesso!");

			return "redirect:/titulos/novo";

		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			erros.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}

	}

	@RequestMapping
	public ModelAndView searchTitulos(@ModelAttribute("filtro") TituloFilter filtro) {
			//(@RequestParam(defaultValue = "%") String descricao)
		
		List<Titulo> allTitulos = cadastroTituloService.filtrar(filtro);
		
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", allTitulos);

		return mv;
	}

	@RequestMapping("{codigo}")
	public ModelAndView edit(@PathVariable("codigo") Titulo titulo /* Long codigo */) {

		/*
		 * Titulo titulo = titulos.findOne(codigo); Não é necessário fazer o findOne
		 * Sprig consegue identificar que estamos bucando o codigo pelo mapeamento,
		 * porque esta sendo o jpa repository
		 */

		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
	}

	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long codigo, RedirectAttributes attributes) {

		cadastroTituloService.delete(codigo);

		attributes.addFlashAttribute("message", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}

	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		// System.out.println(">>> codigo: "+codigo);
		return cadastroTituloService.receber(codigo);

	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}

}
