package br.com.sistemas.chamados.web;

import br.com.sistemas.chamados.dto.TecnicoRequest;
import br.com.sistemas.chamados.enums.PerfilEnum;
import br.com.sistemas.chamados.service.TecnicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/tecnicos")
public class TecnicoWebController {

    private final TecnicoService tecnicoService;

    public TecnicoWebController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @ModelAttribute("perfis")
    public PerfilEnum[] perfis() {
        return PerfilEnum.values();
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tecnicos", tecnicoService.listar());
        return "tecnicos/lista";
    }

    @PostMapping
    public String criar(@ModelAttribute TecnicoRequest request, RedirectAttributes redirect) {
        tecnicoService.criar(request);
        redirect.addFlashAttribute("sucesso", "Técnico cadastrado com sucesso");
        return "redirect:/web/tecnicos";
    }

    @GetMapping("/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("tecnico", tecnicoService.buscar(id));
        return "tecnicos/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute TecnicoRequest request, RedirectAttributes redirect) {
        tecnicoService.atualizar(id, request);
        redirect.addFlashAttribute("sucesso", "Técnico atualizado com sucesso");
        return "redirect:/web/tecnicos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        tecnicoService.excluir(id);
        redirect.addFlashAttribute("sucesso", "Técnico excluído com sucesso");
        return "redirect:/web/tecnicos";
    }
}
