package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.dto.ClienteRequest;
import br.com.sistemas.chamados.dto.ClienteResponse;
import br.com.sistemas.chamados.service.ChamadosService;
import br.com.sistemas.chamados.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ChamadosService chamadosService;

    public ClienteController(ClienteService clienteService, ChamadosService chamadosService) {
        this.clienteService = clienteService;
        this.chamadosService = chamadosService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest clienteRequest){
        ClienteResponse criado = clienteService.criar(clienteRequest);
        return ResponseEntity.created(URI.create("/clientes/" + criado.id())).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar(){
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscar(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.buscar(id));
    }

    @GetMapping("/{id}/chamados")
    public ResponseEntity<PagedModel<ChamadosResponse>> listarChamados(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        return ResponseEntity.ok(new PagedModel<>(chamadosService.listarPorCliente(id, pageable)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
