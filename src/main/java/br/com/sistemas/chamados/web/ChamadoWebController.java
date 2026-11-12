package br.com.sistemas.chamados.web;

import br.com.sistemas.chamados.dto.AlterarStatusRequest;
import br.com.sistemas.chamados.dto.AtribuirTecnicoRequest;
import br.com.sistemas.chamados.dto.ChamadosDetalheResponse;
import br.com.sistemas.chamados.dto.ChamadosFiltro;
import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.dto.ComentarioRequest;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import br.com.sistemas.chamados.service.ChamadosService;
import br.com.sistemas.chamados.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Controller
@RequestMapping("/web/chamados")
public class ChamadoWebController {

    private final ChamadosService chamadosService;
    private final ComentarioService comentarioService;
    private final Cadastros cadastros;

    public ChamadoWebController(ChamadosService chamadosService, ComentarioService comentarioService, Cadastros cadastros) {
        this.chamadosService = chamadosService;
        this.comentarioService = comentarioService;
        this.cadastros = cadastros;
    }

    @ModelAttribute
    public void enums(Model model) {
        model.addAttribute("statusLista", StatusChamado.values());
        model.addAttribute("prioridades", PrioridadeEnum.values());
    }

    @GetMapping
    public String listar(@ModelAttribute("filtro") ChamadosFiltro filtro,
                         @PageableDefault(size = 10, sort = "id") Pageable pageable,
                         Model model) {
        cadastros.adicionar(model);
        model.addAttribute("pagina", chamadosService.listar(filtro, pageable));
        model.addAttribute("urlPaginacao", urlComFiltro(filtro));
        return "chamados/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        cadastros.adicionar(model);
        return "chamados/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute ChamadosRequest request, RedirectAttributes redirect) {
        ChamadosResponse criado = chamadosService.criar(request);
        redirect.addFlashAttribute("sucesso", "Chamado aberto com sucesso");
        return "redirect:/web/chamados/" + criado.id();
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        ChamadosDetalheResponse chamado = chamadosService.buscar(id);
        cadastros.adicionar(model);
        model.addAttribute("chamado", chamado);
        model.addAttribute("transicoes", chamado.status().proximosPermitidos().stream().sorted().toList());
        return "chamados/detalhe";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        cadastros.adicionar(model);
        model.addAttribute("chamado", chamadosService.buscar(id));
        return "chamados/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute ChamadosRequest request, RedirectAttributes redirect) {
        chamadosService.atualizar(id, request);
        redirect.addFlashAttribute("sucesso", "Chamado atualizado com sucesso");
        return "redirect:/web/chamados/" + id;
    }

    @PostMapping("/{id}/comentarios")
    public String comentar(@PathVariable Long id, @Valid @ModelAttribute ComentarioRequest request, RedirectAttributes redirect) {
        comentarioService.adicionar(id, request);
        redirect.addFlashAttribute("sucesso", "Comentário adicionado");
        return "redirect:/web/chamados/" + id;
    }

    @PostMapping("/{id}/atribuir")
    public String atribuir(@PathVariable Long id, @Valid @ModelAttribute AtribuirTecnicoRequest request, RedirectAttributes redirect) {
        chamadosService.atribuirTecnico(id, request);
        redirect.addFlashAttribute("sucesso", "Técnico atribuído");
        return "redirect:/web/chamados/" + id;
    }

    @PostMapping("/{id}/status")
    public String alterarStatus(@PathVariable Long id, @Valid @ModelAttribute AlterarStatusRequest request, RedirectAttributes redirect) {
        chamadosService.alterarStatus(id, request);
        redirect.addFlashAttribute("sucesso", "Status alterado para " + request.status());
        return "redirect:/web/chamados/" + id;
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        chamadosService.excluir(id);
        redirect.addFlashAttribute("sucesso", "Chamado excluído");
        return "redirect:/web/chamados";
    }

    private String urlComFiltro(ChamadosFiltro filtro) {
        return UriComponentsBuilder.fromPath("/web/chamados")
                .queryParamIfPresent("status", Optional.ofNullable(filtro.status()))
                .queryParamIfPresent("prioridade", Optional.ofNullable(filtro.prioridade()))
                .queryParamIfPresent("clienteId", Optional.ofNullable(filtro.clienteId()))
                .queryParamIfPresent("tecnicoId", Optional.ofNullable(filtro.tecnicoId()))
                .queryParamIfPresent("categoriaId", Optional.ofNullable(filtro.categoriaId()))
                .build()
                .toUriString();
    }
}
