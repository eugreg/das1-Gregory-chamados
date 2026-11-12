package br.com.sistemas.chamados.web;

import br.com.sistemas.chamados.dto.CategoriasResponse;
import br.com.sistemas.chamados.dto.ClienteResponse;
import br.com.sistemas.chamados.dto.TecnicoResponse;
import br.com.sistemas.chamados.service.CategoriasService;
import br.com.sistemas.chamados.service.ClienteService;
import br.com.sistemas.chamados.service.TecnicoService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Cadastros {

    private final ClienteService clienteService;
    private final TecnicoService tecnicoService;
    private final CategoriasService categoriasService;

    public Cadastros(ClienteService clienteService, TecnicoService tecnicoService, CategoriasService categoriasService) {
        this.clienteService = clienteService;
        this.tecnicoService = tecnicoService;
        this.categoriasService = categoriasService;
    }

    public void adicionar(Model model) {
        List<ClienteResponse> clientes = clienteService.listar();
        List<TecnicoResponse> tecnicos = tecnicoService.listar();
        List<CategoriasResponse> categorias = categoriasService.listar();

        model.addAttribute("clientes", clientes);
        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("nomesClientes", nomes(clientes, ClienteResponse::id, ClienteResponse::nome));
        model.addAttribute("nomesTecnicos", nomes(tecnicos, TecnicoResponse::id, TecnicoResponse::nome));
        model.addAttribute("nomesCategorias", nomes(categorias, CategoriasResponse::id, CategoriasResponse::nome));
    }

    private <T> Map<Long, String> nomes(List<T> itens, Function<T, Long> id, Function<T, String> nome) {
        return itens.stream().collect(Collectors.toMap(id, nome));
    }
}
