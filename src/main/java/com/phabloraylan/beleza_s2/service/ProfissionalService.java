package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.ProfissionalRequestDTO;
import com.phabloraylan.beleza_s2.dto.ProfissionalResponseDTO;
import com.phabloraylan.beleza_s2.entity.Estabelecimento;
import com.phabloraylan.beleza_s2.entity.Profissional;
import com.phabloraylan.beleza_s2.repository.EstabelecimentoRepository;
import com.phabloraylan.beleza_s2.repository.ProfissionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> findAll() {
        return profissionalRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfissionalResponseDTO findById(Long id) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado com id: " + id));
        return toResponseDTO(profissional);
    }

    @Transactional
    public ProfissionalResponseDTO save(ProfissionalRequestDTO profissionalDTO) {
        Profissional profissional = new Profissional();
        profissional.setNome(profissionalDTO.getNome());
        profissional.setEspecialidades(profissionalDTO.getEspecialidades());
        profissional.setHorariosDisponiveis(profissionalDTO.getHorariosDisponiveis());
        profissional.setTarifa(profissionalDTO.getTarifa());
        
        if (profissionalDTO.getEstabelecimentoId() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(profissionalDTO.getEstabelecimentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado"));
            profissional.setEstabelecimento(estabelecimento);
        }
        
        Profissional savedProfissional = profissionalRepository.save(profissional);
        return toResponseDTO(savedProfissional);
    }

    @Transactional
    public ProfissionalResponseDTO update(Long id, ProfissionalRequestDTO profissionalDTO) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado com id: " + id));
        
        profissional.setNome(profissionalDTO.getNome());
        profissional.setEspecialidades(profissionalDTO.getEspecialidades());
        profissional.setHorariosDisponiveis(profissionalDTO.getHorariosDisponiveis());
        profissional.setTarifa(profissionalDTO.getTarifa());
        
        if (profissionalDTO.getEstabelecimentoId() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(profissionalDTO.getEstabelecimentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado"));
            profissional.setEstabelecimento(estabelecimento);
        } else {
            profissional.setEstabelecimento(null);
        }
        
        Profissional updatedProfissional = profissionalRepository.save(profissional);
        return toResponseDTO(updatedProfissional);
    }

    @Transactional
    public void delete(Long id) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado com id: " + id));
        profissionalRepository.delete(profissional);
    }

    private ProfissionalResponseDTO toResponseDTO(Profissional profissional) {
        ProfissionalResponseDTO dto = new ProfissionalResponseDTO();
        dto.setId(profissional.getId());
        dto.setNome(profissional.getNome());
        dto.setEspecialidades(profissional.getEspecialidades());
        dto.setHorariosDisponiveis(profissional.getHorariosDisponiveis());
        dto.setTarifa(profissional.getTarifa());
        if (profissional.getEstabelecimento() != null) {
            dto.setEstabelecimentoId(profissional.getEstabelecimento().getId());
            dto.setEstabelecimentoNome(profissional.getEstabelecimento().getNome());
        }
        dto.setDataCriacao(profissional.getDataCriacao());
        dto.setDataAtualizacao(profissional.getDataAtualizacao());
        return dto;
    }
}
