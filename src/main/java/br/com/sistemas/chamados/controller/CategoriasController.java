package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.CategoriasRequest;
import br.com.sistemas.chamados.dto.CategoriasResponse;
import br.com.sistemas.chamados.entity.CategoriasEntity;
import br.com.sistemas.chamados.service.CategoriasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriasController {

    private final CategoriasService categoriasService;

    public CategoriasController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }

    @PostMapping
    public ResponseEntity<CategoriasResponse> salvar(@RequestBody CategoriasRequest categorias) {
        CategoriasResponse categoriasResponse = categoriasService.criar(categorias);
        return ResponseEntity.created(URI.create("/categorias/" + categoriasResponse.id())).body(categoriasResponse);
    }

    @GetMapping
    public ResponseEntity<List<CategoriasResponse>> listar() {
        return ResponseEntity.ok(categoriasService.listar());
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        categoriasService.remover(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriasResponse> buscarEntity(@PathVariable Long id) {
        return ResponseEntity.ok(categoriasService.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriasResponse> atualizar(@PathVariable Long id, @RequestBody CategoriasRequest categorias) {
        return ResponseEntity.ok(categoriasService.atualizar(id, categorias));
    }

}
