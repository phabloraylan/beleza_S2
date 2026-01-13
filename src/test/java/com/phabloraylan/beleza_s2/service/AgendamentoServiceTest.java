package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.AgendamentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.AgendamentoResponseDTO;
import com.phabloraylan.beleza_s2.entity.*;
import com.phabloraylan.beleza_s2.repository.AgendamentoRepository;
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
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProfissionalRepository profissionalRepository;
    @Mock
    private EstabelecimentoRepository estabelecimentoRepository;
    @Mock
    private NotificacaoService notificacaoService;
    @Mock
    private IntegracaoCalendarioService integracaoCalendarioService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Agendamento agendamento;
    private AgendamentoRequestDTO agendamentoRequestDTO;
    private Usuario usuario;
    private Profissional profissional;
    private Estabelecimento estabelecimento;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Maria Cabeleireira");

        estabelecimento = new Estabelecimento();
        estabelecimento.setId(1L);
        estabelecimento.setNome("Salão Beleza");

        agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setUsuario(usuario);
        agendamento.setProfissional(profissional);
        agendamento.setEstabelecimento(estabelecimento);
        agendamento.setDataHora(LocalDateTime.now().plusDays(1));
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setDataCriacao(LocalDateTime.now());
        agendamento.setDataAtualizacao(LocalDateTime.now());

        agendamentoRequestDTO = new AgendamentoRequestDTO();
        agendamentoRequestDTO.setUsuarioId(1L);
        agendamentoRequestDTO.setProfissionalId(1L);
        agendamentoRequestDTO.setEstabelecimentoId(1L);
        agendamentoRequestDTO.setDataHora(LocalDateTime.now().plusDays(1));
    }

    @Test
    @DisplayName("Deve retornar lista de agendamentos")
    void findAll_ShouldReturnListOfAgendamentos() {
        when(agendamentoRepository.findAll()).thenReturn(Collections.singletonList(agendamento));

        List<AgendamentoResponseDTO> result = agendamentoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(agendamento.getId(), result.get(0).getId());
        verify(agendamentoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar agendamento por ID")
    void findById_ShouldReturnAgendamento_WhenIdExists() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        AgendamentoResponseDTO result = agendamentoService.findById(1L);

        assertNotNull(result);
        assertEquals(agendamento.getId(), result.getId());
        verify(agendamentoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID não existe")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> agendamentoService.findById(1L));
        verify(agendamentoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve salvar novo agendamento")
    void save_ShouldReturnSavedAgendamento() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponseDTO result = agendamentoService.save(agendamentoRequestDTO);

        assertNotNull(result);
        assertEquals(agendamento.getId(), result.getId());
        verify(notificacaoService, times(1)).enviarConfirmacaoAgendamento(any(Agendamento.class));
        verify(integracaoCalendarioService, times(1)).sincronizarCalendario(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve atualizar agendamento existente")
    void update_ShouldReturnUpdatedAgendamento_WhenIdExists() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(estabelecimentoRepository.findById(1L)).thenReturn(Optional.of(estabelecimento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponseDTO result = agendamentoService.update(1L, agendamentoRequestDTO);

        assertNotNull(result);
        assertEquals(agendamento.getId(), result.getId());
        verify(integracaoCalendarioService, times(1)).sincronizarCalendario(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve deletar agendamento")
    void delete_ShouldDeleteAgendamento_WhenIdExists() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        doNothing().when(agendamentoRepository).delete(agendamento);

        agendamentoService.delete(1L);

        verify(integracaoCalendarioService, times(1)).removerDoCalendario(agendamento);
        verify(agendamentoRepository, times(1)).delete(agendamento);
    }
}
