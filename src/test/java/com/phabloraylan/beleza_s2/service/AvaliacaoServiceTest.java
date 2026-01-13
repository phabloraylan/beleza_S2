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
class AvaliacaoServiceTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private EstabelecimentoRepository estabelecimentoRepository;
    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    private Avaliacao avaliacao;
    private AvaliacaoRequestDTO avaliacaoRequestDTO;
    private Usuario usuario;
    private Estabelecimento estabelecimento;
    private Profissional profissional;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");

        estabelecimento = new Estabelecimento();
        estabelecimento.setId(1L);
        estabelecimento.setNome("Salão Beleza");

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Maria Cabeleireira");

        avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setUsuario(usuario);
        avaliacao.setEstabelecimento(estabelecimento);
        avaliacao.setProfissional(profissional);
        avaliacao.setNota(5);
        avaliacao.setComentario("Excelente serviço!");
        avaliacao.setDataCriacao(LocalDateTime.now());
        avaliacao.setDataAtualizacao(LocalDateTime.now());

        avaliacaoRequestDTO = new AvaliacaoRequestDTO();
        avaliacaoRequestDTO.setUsuarioId(1L);
        avaliacaoRequestDTO.setEstabelecimentoId(1L);
        avaliacaoRequestDTO.setProfissionalId(1L);
        avaliacaoRequestDTO.setNota(5);
        avaliacaoRequestDTO.setComentario("Excelente serviço!");
    }

    @Test
    @DisplayName("Deve retornar lista de avaliações")
    void findAll_ShouldReturnListOfAvaliacoes() {
        when(avaliacaoRepository.findAll()).thenReturn(Collections.singletonList(avaliacao));

        List<AvaliacaoResponseDTO> result = avaliacaoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(avaliacao.getNota(), result.get(0).getNota());
        verify(avaliacaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar avaliação por ID")
    void findById_ShouldReturnAvaliacao_WhenIdExists() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

        AvaliacaoResponseDTO result = avaliacaoService.findById(1L);

        assertNotNull(result);
        assertEquals(avaliacao.getId(), result.getId());
        assertEquals(avaliacao.getNota(), result.getNota());
        verify(avaliacaoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID não existe")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> avaliacaoService.findById(1L));
        verify(avaliacaoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve salvar nova avaliação")
    void save_ShouldReturnSavedAvaliacao() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        AvaliacaoResponseDTO result = avaliacaoService.save(avaliacaoRequestDTO);

        assertNotNull(result);
        assertEquals(avaliacao.getNota(), result.getNota());
        verify(avaliacaoRepository, times(1)).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("Deve atualizar avaliação existente")
    void update_ShouldReturnUpdatedAvaliacao_WhenIdExists() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        AvaliacaoResponseDTO result = avaliacaoService.update(1L, avaliacaoRequestDTO);

        assertNotNull(result);
        assertEquals(avaliacao.getNota(), result.getNota());
        verify(avaliacaoRepository, times(1)).findById(1L);
        verify(avaliacaoRepository, times(1)).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ID inexistente")
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> avaliacaoService.update(1L, avaliacaoRequestDTO));
        verify(avaliacaoRepository, times(1)).findById(1L);
        verify(avaliacaoRepository, never()).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("Deve deletar avaliação")
    void delete_ShouldDeleteAvaliacao_WhenIdExists() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        doNothing().when(avaliacaoRepository).delete(avaliacao);

        avaliacaoService.delete(1L);

        verify(avaliacaoRepository, times(1)).findById(1L);
        verify(avaliacaoRepository, times(1)).delete(avaliacao);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ID inexistente")
    void delete_ShouldThrowException_WhenIdDoesNotExist() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> avaliacaoService.delete(1L));
        verify(avaliacaoRepository, times(1)).findById(1L);
        verify(avaliacaoRepository, never()).delete(any(Avaliacao.class));
    }
}
