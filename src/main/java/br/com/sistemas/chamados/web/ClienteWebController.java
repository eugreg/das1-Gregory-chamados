package br.com.sistemas.chamados.web;

import br.com.sistemas.chamados.dto.ClienteRequest;
import br.com.sistemas.chamados.service.ChamadosService;
import br.com.sistemas.chamados.service.ClienteService;
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

@Controller
@RequestMapping("/web/clientes")
public class ClienteWebController {

    private final ClienteService clienteService;
    private final ChamadosService chamadosService;
    private final Cadastros cadastros;

    public ClienteWebController(ClienteService clienteService, ChamadosService chamadosService, Cadastros cadastros) {
        this.clienteService = clienteService;
        this.chamadosService = chamadosService;
        this.cadastros = cadastros;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listar());
        return "clientes/lista";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute ClienteRequest request, RedirectAttributes redirect) {
        clienteService.criar(request);
        redirect.addFlashAttribute("sucesso", "Cliente cadastrado com sucesso");
        return "redirect:/web/clientes";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id,
                           @PageableDefault(size = 10, sort = "id") Pageable pageable,
                           Model model) {
        cadastros.adicionar(model);
        model.addAttribute("cliente", clienteService.buscar(id));
        model.addAttribute("pagina", chamadosService.listarPorCliente(id, pageable));
        return "clientes/detalhe";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        clienteService.excluir(id);
        redirect.addFlashAttribute("sucesso", "Cliente excluído com sucesso");
        return "redirect:/web/clientes";
    }
}
