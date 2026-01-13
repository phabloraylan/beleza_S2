package com.phabloraylan.beleza_s2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phabloraylan.beleza_s2.dto.EstabelecimentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.EstabelecimentoResponseDTO;
import com.phabloraylan.beleza_s2.service.EstabelecimentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstabelecimentoController.class)
class EstabelecimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private ObjectMapper objectMapper;

    private EstabelecimentoResponseDTO estabelecimentoResponseDTO;
    private EstabelecimentoRequestDTO estabelecimentoRequestDTO;

    @BeforeEach
    void setUp() {
        estabelecimentoResponseDTO = new EstabelecimentoResponseDTO();
        estabelecimentoResponseDTO.setId(1L);
        estabelecimentoResponseDTO.setNome("Salão Beleza");
        estabelecimentoResponseDTO.setEndereco("Rua das Flores, 123");
        estabelecimentoResponseDTO.setDescricao("O melhor salão da cidade");
        estabelecimentoResponseDTO.setHorarioFuncionamento("08:00 - 18:00");
        estabelecimentoResponseDTO.setServicos(Arrays.asList("Corte", "Manicure"));
        estabelecimentoResponseDTO.setFotos(Collections.singletonList("foto1.jpg"));

        estabelecimentoRequestDTO = new EstabelecimentoRequestDTO();
        estabelecimentoRequestDTO.setNome("Salão Beleza");
        estabelecimentoRequestDTO.setEndereco("Rua das Flores, 123");
        estabelecimentoRequestDTO.setDescricao("O melhor salão da cidade");
        estabelecimentoRequestDTO.setHorarioFuncionamento("08:00 - 18:00");
        estabelecimentoRequestDTO.setServicos(Arrays.asList("Corte", "Manicure"));
        estabelecimentoRequestDTO.setFotos(Collections.singletonList("foto1.jpg"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve listar todos os estabelecimentos")
    void findAll_ShouldReturnListOfEstabelecimentos() throws Exception {
        when(estabelecimentoService.findAll()).thenReturn(Collections.singletonList(estabelecimentoResponseDTO));

        mockMvc.perform(get("/api/estabelecimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(estabelecimentoResponseDTO.getId()))
                .andExpect(jsonPath("$[0].nome").value(estabelecimentoResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar estabelecimento por ID")
    void findById_ShouldReturnEstabelecimento() throws Exception {
        when(estabelecimentoService.findById(1L)).thenReturn(estabelecimentoResponseDTO);

        mockMvc.perform(get("/api/estabelecimentos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(estabelecimentoResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(estabelecimentoResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar estabelecimentos com filtros")
    void search_ShouldReturnFilteredEstabelecimentos() throws Exception {
        when(estabelecimentoService.search(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(estabelecimentoResponseDTO));

        mockMvc.perform(get("/api/estabelecimentos/busca")
                        .param("nome", "Salão"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(estabelecimentoResponseDTO.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar novo estabelecimento")
    void create_ShouldReturnCreatedEstabelecimento() throws Exception {
        when(estabelecimentoService.save(any(EstabelecimentoRequestDTO.class))).thenReturn(estabelecimentoResponseDTO);

        mockMvc.perform(post("/api/estabelecimentos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estabelecimentoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(estabelecimentoResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(estabelecimentoResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar estabelecimento")
    void update_ShouldReturnUpdatedEstabelecimento() throws Exception {
        when(estabelecimentoService.update(eq(1L), any(EstabelecimentoRequestDTO.class))).thenReturn(estabelecimentoResponseDTO);

        mockMvc.perform(put("/api/estabelecimentos/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estabelecimentoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(estabelecimentoResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(estabelecimentoResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve deletar estabelecimento")
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(estabelecimentoService).delete(1L);

        mockMvc.perform(delete("/api/estabelecimentos/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
