
package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.service.ChamadosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadosController {

    private final ChamadosService chamadosService;

    public ChamadosController(ChamadosService chamadosService) {
        this.chamadosService = chamadosService;
    }

    @PostMapping
    public ResponseEntity<ChamadosResponse> criar(
            @Valid @RequestBody ChamadosRequest chamadosRequest) {

        ChamadosResponse criado = chamadosService.criar(chamadosRequest);

        return ResponseEntity
                .created(URI.create("/chamados/" + criado.id()))
                .body(criado);
    }

    @GetMapping
    public ResponseEntity<List<ChamadosResponse>> listar() {
        return ResponseEntity.ok(chamadosService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadosResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(chamadosService.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChamadosResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ChamadosRequest chamadosRequest) {

        return ResponseEntity.ok(
                chamadosService.atualizar(id, chamadosRequest)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        chamadosService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
