package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.AlterarStatusRequest;
import br.com.sistemas.chamados.dto.AtribuirTecnicoRequest;
import br.com.sistemas.chamados.dto.ChamadosFiltro;
import br.com.sistemas.chamados.dto.ChamadosDetalheResponse;
import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.dto.ComentarioRequest;
import br.com.sistemas.chamados.dto.ComentarioResponse;
import br.com.sistemas.chamados.service.ChamadosService;
import br.com.sistemas.chamados.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/chamados")
public class ChamadosController {

    private final ChamadosService chamadosService;
    private final ComentarioService comentarioService;

    public ChamadosController(ChamadosService chamadosService, ComentarioService comentarioService) {
        this.chamadosService = chamadosService;
        this.comentarioService = comentarioService;
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
    public ResponseEntity<PagedModel<ChamadosResponse>> listar(
            ChamadosFiltro filtro,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        return ResponseEntity.ok(new PagedModel<>(chamadosService.listar(filtro, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadosDetalheResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(chamadosService.buscar(id));
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioResponse> comentar(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.adicionar(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChamadosResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ChamadosRequest chamadosRequest) {

        return ResponseEntity.ok(
                chamadosService.atualizar(id, chamadosRequest)
        );
    }

    @PatchMapping("/{id}/atribuir")
    public ResponseEntity<ChamadosResponse> atribuir(
            @PathVariable Long id,
            @Valid @RequestBody AtribuirTecnicoRequest request) {

        return ResponseEntity.ok(chamadosService.atribuirTecnico(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadosResponse> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlterarStatusRequest request) {

        return ResponseEntity.ok(chamadosService.alterarStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        chamadosService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
