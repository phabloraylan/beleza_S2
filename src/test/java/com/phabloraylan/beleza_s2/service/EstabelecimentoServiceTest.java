package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.EstabelecimentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.EstabelecimentoResponseDTO;
import com.phabloraylan.beleza_s2.entity.Estabelecimento;
import com.phabloraylan.beleza_s2.repository.EstabelecimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstabelecimentoServiceTest {

    @Mock
    private EstabelecimentoRepository estabelecimentoRepository;

    @InjectMocks
    private EstabelecimentoService estabelecimentoService;

    private Estabelecimento estabelecimento;
    private EstabelecimentoRequestDTO estabelecimentoRequestDTO;

    @BeforeEach
    void setUp() {
        estabelecimento = new Estabelecimento();
        estabelecimento.setId(1L);
        estabelecimento.setNome("Salão Beleza");
        estabelecimento.setEndereco("Rua das Flores, 123");
        estabelecimento.setDescricao("O melhor salão da cidade");
        estabelecimento.setHorarioFuncionamento("08:00 - 18:00");
        estabelecimento.setServicos(Arrays.asList("Corte", "Manicure"));
        estabelecimento.setFotos(Collections.singletonList("foto1.jpg"));
        estabelecimento.setDataCriacao(LocalDateTime.now());
        estabelecimento.setDataAtualizacao(LocalDateTime.now());

        estabelecimentoRequestDTO = new EstabelecimentoRequestDTO();
        estabelecimentoRequestDTO.setNome("Salão Beleza");
        estabelecimentoRequestDTO.setEndereco("Rua das Flores, 123");
        estabelecimentoRequestDTO.setDescricao("O melhor salão da cidade");
        estabelecimentoRequestDTO.setHorarioFuncionamento("08:00 - 18:00");
        estabelecimentoRequestDTO.setServicos(Arrays.asList("Corte", "Manicure"));
        estabelecimentoRequestDTO.setFotos(Collections.singletonList("foto1.jpg"));
    }

    @Test
    @DisplayName("Deve retornar lista de estabelecimentos")
    void findAll_ShouldReturnListOfEstabelecimentos() {
        when(estabelecimentoRepository.findAll()).thenReturn(Collections.singletonList(estabelecimento));

        List<EstabelecimentoResponseDTO> result = estabelecimentoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(estabelecimento.getNome(), result.get(0).getNome());
        verify(estabelecimentoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar estabelecimento por ID")
    void findById_ShouldReturnEstabelecimento_WhenIdExists() {
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));

        EstabelecimentoResponseDTO result = estabelecimentoService.findById(1L);

        assertNotNull(result);
        assertEquals(estabelecimento.getId(), result.getId());
        assertEquals(estabelecimento.getNome(), result.getNome());
        verify(estabelecimentoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID não existe")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> estabelecimentoService.findById(1L));
        verify(estabelecimentoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve buscar estabelecimentos com filtros")
    void search_ShouldReturnFilteredEstabelecimentos() {
        when(estabelecimentoRepository.search(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(estabelecimento));

        List<EstabelecimentoResponseDTO> result = estabelecimentoService.search("Salão", null, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(estabelecimentoRepository, times(1)).search("Salão", null, null, null, null);
    }

    @Test
    @DisplayName("Deve salvar novo estabelecimento")
    void save_ShouldReturnSavedEstabelecimento() {
        when(estabelecimentoRepository.save(any(Estabelecimento.class))).thenReturn(estabelecimento);

        EstabelecimentoResponseDTO result = estabelecimentoService.save(estabelecimentoRequestDTO);

        assertNotNull(result);
        assertEquals(estabelecimento.getNome(), result.getNome());
        verify(estabelecimentoRepository, times(1)).save(any(Estabelecimento.class));
    }

    @Test
    @DisplayName("Deve atualizar estabelecimento existente")
    void update_ShouldReturnUpdatedEstabelecimento_WhenIdExists() {
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        when(estabelecimentoRepository.save(any(Estabelecimento.class))).thenReturn(estabelecimento);

        EstabelecimentoResponseDTO result = estabelecimentoService.update(1L, estabelecimentoRequestDTO);

        assertNotNull(result);
        assertEquals(estabelecimento.getNome(), result.getNome());
        verify(estabelecimentoRepository, times(1)).findById(1L);
        verify(estabelecimentoRepository, times(1)).save(any(Estabelecimento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ID inexistente")
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> estabelecimentoService.update(1L, estabelecimentoRequestDTO));
        verify(estabelecimentoRepository, times(1)).findById(1L);
        verify(estabelecimentoRepository, never()).save(any(Estabelecimento.class));
    }

    @Test
    @DisplayName("Deve deletar estabelecimento")
    void delete_ShouldDeleteEstabelecimento_WhenIdExists() {
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        doNothing().when(estabelecimentoRepository).delete(estabelecimento);

        estabelecimentoService.delete(1L);

        verify(estabelecimentoRepository, times(1)).findById(1L);
        verify(estabelecimentoRepository, times(1)).delete(estabelecimento);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ID inexistente")
    void delete_ShouldThrowException_WhenIdDoesNotExist() {
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> estabelecimentoService.delete(1L));
        verify(estabelecimentoRepository, times(1)).findById(1L);
        verify(estabelecimentoRepository, never()).delete(any(Estabelecimento.class));
    }
}
