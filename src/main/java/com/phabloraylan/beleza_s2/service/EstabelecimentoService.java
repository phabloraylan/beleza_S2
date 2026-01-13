package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.EstabelecimentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.EstabelecimentoResponseDTO;
import com.phabloraylan.beleza_s2.entity.Estabelecimento;
import com.phabloraylan.beleza_s2.repository.EstabelecimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final EstabelecimentoRepository estabelecimentoRepository;

    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> findAll() {
        return estabelecimentoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstabelecimentoResponseDTO findById(Long id) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com id: " + id));
        return toResponseDTO(estabelecimento);
    }

    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> search(String nome, String endereco, String servico, Double precoMinimo, Double precoMaximo) {
        return estabelecimentoRepository.search(nome, endereco, servico, precoMinimo, precoMaximo).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EstabelecimentoResponseDTO save(EstabelecimentoRequestDTO estabelecimentoDTO) {
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setNome(estabelecimentoDTO.getNome());
        estabelecimento.setEndereco(estabelecimentoDTO.getEndereco());
        estabelecimento.setDescricao(estabelecimentoDTO.getDescricao());
        estabelecimento.setHorarioFuncionamento(estabelecimentoDTO.getHorarioFuncionamento());
        estabelecimento.setServicos(estabelecimentoDTO.getServicos());
        estabelecimento.setFotos(estabelecimentoDTO.getFotos());
        
        Estabelecimento savedEstabelecimento = estabelecimentoRepository.save(estabelecimento);
        return toResponseDTO(savedEstabelecimento);
    }

    @Transactional
    public EstabelecimentoResponseDTO update(Long id, EstabelecimentoRequestDTO estabelecimentoDTO) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com id: " + id));
        
        estabelecimento.setNome(estabelecimentoDTO.getNome());
        estabelecimento.setEndereco(estabelecimentoDTO.getEndereco());
        estabelecimento.setDescricao(estabelecimentoDTO.getDescricao());
        estabelecimento.setHorarioFuncionamento(estabelecimentoDTO.getHorarioFuncionamento());
        estabelecimento.setServicos(estabelecimentoDTO.getServicos());
        estabelecimento.setFotos(estabelecimentoDTO.getFotos());
        
        Estabelecimento updatedEstabelecimento = estabelecimentoRepository.save(estabelecimento);
        return toResponseDTO(updatedEstabelecimento);
    }

    @Transactional
    public void delete(Long id) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com id: " + id));
        estabelecimentoRepository.delete(estabelecimento);
    }

    private EstabelecimentoResponseDTO toResponseDTO(Estabelecimento estabelecimento) {
        EstabelecimentoResponseDTO dto = new EstabelecimentoResponseDTO();
        dto.setId(estabelecimento.getId());
        dto.setNome(estabelecimento.getNome());
        dto.setEndereco(estabelecimento.getEndereco());
        dto.setDescricao(estabelecimento.getDescricao());
        dto.setHorarioFuncionamento(estabelecimento.getHorarioFuncionamento());
        dto.setServicos(estabelecimento.getServicos());
        dto.setFotos(estabelecimento.getFotos());
        dto.setDataCriacao(estabelecimento.getDataCriacao());
        dto.setDataAtualizacao(estabelecimento.getDataAtualizacao());
        return dto;
    }
}
