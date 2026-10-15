package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.CategoriasRequest;
import br.com.sistemas.chamados.dto.CategoriasResponse;
import br.com.sistemas.chamados.entity.CategoriasEntity;
import br.com.sistemas.chamados.service.CategoriasService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriasController {

    private final CategoriasService categoriasService;

    public CategoriasController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }

    @PostMapping
    public CategoriasResponse salvar(@RequestBody CategoriasRequest categorias){
        return categoriasService.criar(categorias);
    }

    @GetMapping
    public List<CategoriasResponse> listar(){
        return categoriasService.listar();
    }

}
