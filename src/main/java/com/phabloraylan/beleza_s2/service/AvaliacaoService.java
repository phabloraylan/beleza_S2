package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.AvaliacaoRequestDTO;
import com.phabloraylan.beleza_s2.dto.AvaliacaoResponseDTO;
import com.phabloraylan.beleza_s2.entity.Avaliacao;
import com.phabloraylan.beleza_s2.entity.Estabelecimento;
import com.phabloraylan.beleza_s2.entity.Profissional;
import com.phabloraylan.beleza_s2.entity.Usuario;
import com.phabloraylan.beleza_s2.repository.AvaliacaoRepository;
import com.phabloraylan.beleza_s2.repository.EstabelecimentoRepository;
import com.phabloraylan.beleza_s2.repository.ProfissionalRepository;
import com.phabloraylan.beleza_s2.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final ProfissionalRepository profissionalRepository;

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> findAll() {
        return avaliacaoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO findById(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada com id: " + id));
        return toResponseDTO(avaliacao);
    }

    @Transactional
    public AvaliacaoResponseDTO save(AvaliacaoRequestDTO avaliacaoDTO) {
        Usuario usuario = usuarioRepository.findById(avaliacaoDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setUsuario(usuario);
        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());

        if (avaliacaoDTO.getEstabelecimentoId() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(avaliacaoDTO.getEstabelecimentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado"));
            avaliacao.setEstabelecimento(estabelecimento);
        }

        if (avaliacaoDTO.getProfissionalId() != null) {
            Profissional profissional = profissionalRepository.findById(avaliacaoDTO.getProfissionalId())
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
            avaliacao.setProfissional(profissional);
        }

        Avaliacao savedAvaliacao = avaliacaoRepository.save(avaliacao);
        return toResponseDTO(savedAvaliacao);
    }

    @Transactional
    public AvaliacaoResponseDTO update(Long id, AvaliacaoRequestDTO avaliacaoDTO) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada com id: " + id));

        Usuario usuario = usuarioRepository.findById(avaliacaoDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
        avaliacao.setUsuario(usuario);
        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());

        if (avaliacaoDTO.getEstabelecimentoId() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(avaliacaoDTO.getEstabelecimentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado"));
            avaliacao.setEstabelecimento(estabelecimento);
        } else {
            avaliacao.setEstabelecimento(null);
        }

        if (avaliacaoDTO.getProfissionalId() != null) {
            Profissional profissional = profissionalRepository.findById(avaliacaoDTO.getProfissionalId())
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
            avaliacao.setProfissional(profissional);
        } else {
            avaliacao.setProfissional(null);
        }

        Avaliacao updatedAvaliacao = avaliacaoRepository.save(avaliacao);
        return toResponseDTO(updatedAvaliacao);
    }

    @Transactional
    public void delete(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada com id: " + id));
        avaliacaoRepository.delete(avaliacao);
    }

    private AvaliacaoResponseDTO toResponseDTO(Avaliacao avaliacao) {
        AvaliacaoResponseDTO dto = new AvaliacaoResponseDTO();
        dto.setId(avaliacao.getId());
        dto.setUsuarioId(avaliacao.getUsuario().getId());
        dto.setUsuarioNome(avaliacao.getUsuario().getNome());
        
        if (avaliacao.getEstabelecimento() != null) {
            dto.setEstabelecimentoId(avaliacao.getEstabelecimento().getId());
            dto.setEstabelecimentoNome(avaliacao.getEstabelecimento().getNome());
        }
        
        if (avaliacao.getProfissional() != null) {
            dto.setProfissionalId(avaliacao.getProfissional().getId());
            dto.setProfissionalNome(avaliacao.getProfissional().getNome());
        }
        
        dto.setNota(avaliacao.getNota());
        dto.setComentario(avaliacao.getComentario());
        dto.setDataCriacao(avaliacao.getDataCriacao());
        dto.setDataAtualizacao(avaliacao.getDataAtualizacao());
        return dto;
    }
}
