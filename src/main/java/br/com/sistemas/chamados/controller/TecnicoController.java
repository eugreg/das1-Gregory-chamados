package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.TecnicoRequest;
import br.com.sistemas.chamados.dto.TecnicoResponse;
import br.com.sistemas.chamados.service.TecnicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @PostMapping
    public ResponseEntity<TecnicoResponse> salvar(@RequestBody TecnicoRequest tecnico) {
        TecnicoResponse tecnicoResponse = tecnicoService.criar(tecnico);
        return ResponseEntity.created(URI.create("/tecnico/" + tecnicoResponse.id())).body(tecnicoResponse);
    }

    @GetMapping
    public ResponseEntity<List<TecnicoResponse>> listar() {
        return ResponseEntity.ok(tecnicoService.listar());
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        tecnicoService.excluir(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponse> buscarEntity(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoResponse> atualizar(@PathVariable Long id, @RequestBody TecnicoRequest tecnico) {
        return ResponseEntity.ok(tecnicoService.atualizar(id, tecnico));
    }
}
