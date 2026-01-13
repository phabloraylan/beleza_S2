package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.ProfissionalRequestDTO;
import com.phabloraylan.beleza_s2.dto.ProfissionalResponseDTO;
import com.phabloraylan.beleza_s2.entity.Estabelecimento;
import com.phabloraylan.beleza_s2.entity.Profissional;
import com.phabloraylan.beleza_s2.repository.EstabelecimentoRepository;
import com.phabloraylan.beleza_s2.repository.ProfissionalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;
    @Mock
    private EstabelecimentoRepository estabelecimentoRepository;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Profissional profissional;
    private ProfissionalRequestDTO profissionalRequestDTO;
    private Estabelecimento estabelecimento;

    @BeforeEach
    void setUp() {
        estabelecimento = new Estabelecimento();
        estabelecimento.setId(1L);
        estabelecimento.setNome("Salão Beleza");

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Maria Cabeleireira");
        profissional.setEspecialidades("Corte, Pintura");
        profissional.setHorariosDisponiveis("09:00 - 17:00");
        profissional.setTarifa(50.0);
        profissional.setEstabelecimento(estabelecimento);
        profissional.setDataCriacao(LocalDateTime.now());
        profissional.setDataAtualizacao(LocalDateTime.now());

        profissionalRequestDTO = new ProfissionalRequestDTO();
        profissionalRequestDTO.setNome("Maria Cabeleireira");
        profissionalRequestDTO.setEspecialidades("Corte, Pintura");
        profissionalRequestDTO.setHorariosDisponiveis("09:00 - 17:00");
        profissionalRequestDTO.setTarifa(50.0);
        profissionalRequestDTO.setEstabelecimentoId(1L);
    }

    @Test
    @DisplayName("Deve retornar lista de profissionais")
    void findAll_ShouldReturnListOfProfissionais() {
        when(profissionalRepository.findAll()).thenReturn(Collections.singletonList(profissional));

        List<ProfissionalResponseDTO> result = profissionalService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(profissional.getNome(), result.get(0).getNome());
        verify(profissionalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar profissional por ID")
    void findById_ShouldReturnProfissional_WhenIdExists() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        ProfissionalResponseDTO result = profissionalService.findById(1L);

        assertNotNull(result);
        assertEquals(profissional.getId(), result.getId());
        assertEquals(profissional.getNome(), result.getNome());
        verify(profissionalRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID não existe")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> profissionalService.findById(1L));
        verify(profissionalRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve salvar novo profissional")
    void save_ShouldReturnSavedProfissional() {
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        ProfissionalResponseDTO result = profissionalService.save(profissionalRequestDTO);

        assertNotNull(result);
        assertEquals(profissional.getNome(), result.getNome());
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }

    @Test
    @DisplayName("Deve atualizar profissional existente")
    void update_ShouldReturnUpdatedProfissional_WhenIdExists() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        ProfissionalResponseDTO result = profissionalService.update(1L, profissionalRequestDTO);

        assertNotNull(result);
        assertEquals(profissional.getNome(), result.getNome());
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ID inexistente")
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> profissionalService.update(1L, profissionalRequestDTO));
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalRepository, never()).save(any(Profissional.class));
    }

    @Test
    @DisplayName("Deve deletar profissional")
    void delete_ShouldDeleteProfissional_WhenIdExists() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        doNothing().when(profissionalRepository).delete(profissional);

        profissionalService.delete(1L);

        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).delete(profissional);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ID inexistente")
    void delete_ShouldThrowException_WhenIdDoesNotExist() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> profissionalService.delete(1L));
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalRepository, never()).delete(any(Profissional.class));
    }
}
