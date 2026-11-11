package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.ComentarioRequest;
import br.com.sistemas.chamados.dto.ComentarioResponse;
import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.entity.ComentarioEntity;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.repository.ChamadosRepository;
import br.com.sistemas.chamados.repository.ComentarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComentarioService {

    private final ChamadosRepository chamadosRepository;
    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ChamadosRepository chamadosRepository, ComentarioRepository comentarioRepository) {
        this.chamadosRepository = chamadosRepository;
        this.comentarioRepository = comentarioRepository;
    }

    public ComentarioResponse adicionar(Long chamadoId, ComentarioRequest request) {
        ChamadosEntity chamado = chamadosRepository.findById(chamadoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado " + chamadoId + " não encontrado"));

        ComentarioEntity comentario = chamado.adicionarComentario(request.texto(), request.autor());

        return ComentarioResponse.de(comentarioRepository.save(comentario));
    }
}
