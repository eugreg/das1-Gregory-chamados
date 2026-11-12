package br.com.sistemas.chamados.web;

import br.com.sistemas.chamados.dto.CategoriasRequest;
import br.com.sistemas.chamados.service.CategoriasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/categorias")
public class CategoriaWebController {

    private final CategoriasService categoriasService;

    public CategoriaWebController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriasService.listar());
        return "categorias/lista";
    }

    @PostMapping
    public String criar(@ModelAttribute CategoriasRequest request, RedirectAttributes redirect) {
        categoriasService.criar(request);
        redirect.addFlashAttribute("sucesso", "Categoria cadastrada com sucesso");
        return "redirect:/web/categorias";
    }

    @GetMapping("/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoriasService.buscar(id));
        return "categorias/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute CategoriasRequest request, RedirectAttributes redirect) {
        categoriasService.atualizar(id, request);
        redirect.addFlashAttribute("sucesso", "Categoria atualizada com sucesso");
        return "redirect:/web/categorias";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        categoriasService.remover(id);
        redirect.addFlashAttribute("sucesso", "Categoria excluída com sucesso");
        return "redirect:/web/categorias";
    }
}
