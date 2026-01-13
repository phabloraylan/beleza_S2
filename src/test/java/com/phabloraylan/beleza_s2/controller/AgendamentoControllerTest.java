package com.phabloraylan.beleza_s2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phabloraylan.beleza_s2.dto.AgendamentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.AgendamentoResponseDTO;
import com.phabloraylan.beleza_s2.entity.StatusAgendamento;
import com.phabloraylan.beleza_s2.service.AgendamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgendamentoController.class)
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamentoService agendamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    private AgendamentoResponseDTO agendamentoResponseDTO;
    private AgendamentoRequestDTO agendamentoRequestDTO;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        agendamentoResponseDTO = new AgendamentoResponseDTO();
        agendamentoResponseDTO.setId(1L);
        agendamentoResponseDTO.setUsuarioId(1L);
        agendamentoResponseDTO.setProfissionalId(1L);
        agendamentoResponseDTO.setEstabelecimentoId(1L);
        agendamentoResponseDTO.setDataHora(LocalDateTime.now().plusDays(1));
        agendamentoResponseDTO.setStatus(StatusAgendamento.AGENDADO);

        agendamentoRequestDTO = new AgendamentoRequestDTO();
        agendamentoRequestDTO.setUsuarioId(1L);
        agendamentoRequestDTO.setProfissionalId(1L);
        agendamentoRequestDTO.setEstabelecimentoId(1L);
        agendamentoRequestDTO.setDataHora(LocalDateTime.now().plusDays(1));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve listar todos os agendamentos")
    void findAll_ShouldReturnListOfAgendamentos() throws Exception {
        when(agendamentoService.findAll()).thenReturn(Collections.singletonList(agendamentoResponseDTO));

        mockMvc.perform(get("/api/agendamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(agendamentoResponseDTO.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar agendamento por ID")
    void findById_ShouldReturnAgendamento() throws Exception {
        when(agendamentoService.findById(1L)).thenReturn(agendamentoResponseDTO);

        mockMvc.perform(get("/api/agendamentos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendamentoResponseDTO.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar novo agendamento")
    void create_ShouldReturnCreatedAgendamento() throws Exception {
        when(agendamentoService.save(any(AgendamentoRequestDTO.class))).thenReturn(agendamentoResponseDTO);

        mockMvc.perform(post("/api/agendamentos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(agendamentoResponseDTO.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar agendamento")
    void update_ShouldReturnUpdatedAgendamento() throws Exception {
        when(agendamentoService.update(eq(1L), any(AgendamentoRequestDTO.class))).thenReturn(agendamentoResponseDTO);

        mockMvc.perform(put("/api/agendamentos/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendamentoResponseDTO.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve deletar agendamento")
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(agendamentoService).delete(1L);

        mockMvc.perform(delete("/api/agendamentos/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
