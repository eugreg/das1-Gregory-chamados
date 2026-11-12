package br.com.sistemas.chamados.web;

import br.com.sistemas.chamados.dto.ChamadosFiltro;
import br.com.sistemas.chamados.enums.StatusChamado;
import br.com.sistemas.chamados.service.CategoriasService;
import br.com.sistemas.chamados.service.ChamadosService;
import br.com.sistemas.chamados.service.ClienteService;
import br.com.sistemas.chamados.service.TecnicoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class InicioWebController {

    private static final ChamadosFiltro SEM_FILTRO = new ChamadosFiltro(null, null, null, null, null);

    private final ChamadosService chamadosService;
    private final ClienteService clienteService;
    private final TecnicoService tecnicoService;
    private final CategoriasService categoriasService;
    private final Cadastros cadastros;

    public InicioWebController(ChamadosService chamadosService, ClienteService clienteService,
                               TecnicoService tecnicoService, CategoriasService categoriasService,
                               Cadastros cadastros) {
        this.chamadosService = chamadosService;
        this.clienteService = clienteService;
        this.tecnicoService = tecnicoService;
        this.categoriasService = categoriasService;
        this.cadastros = cadastros;
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/web";
    }

    @GetMapping("/web")
    public String inicio(Model model) {
        Map<StatusChamado, Long> porStatus = new LinkedHashMap<>();
        Arrays.stream(StatusChamado.values()).forEach(status -> porStatus.put(status, contar(status)));

        model.addAttribute("porStatus", porStatus);
        model.addAttribute("totalChamados", contar(null));
        model.addAttribute("totalClientes", clienteService.listar().size());
        model.addAttribute("totalTecnicos", tecnicoService.listar().size());
        model.addAttribute("totalCategorias", categoriasService.listar().size());
        model.addAttribute("recentes", chamadosService.listar(SEM_FILTRO,
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"))).getContent());
        cadastros.adicionar(model);
        return "inicio";
    }

    private long contar(StatusChamado status) {
        ChamadosFiltro filtro = new ChamadosFiltro(status, null, null, null, null);
        return chamadosService.listar(filtro, PageRequest.of(0, 1)).getTotalElements();
    }
}
