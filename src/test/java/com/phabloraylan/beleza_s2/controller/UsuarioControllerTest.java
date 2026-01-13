package com.phabloraylan.beleza_s2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phabloraylan.beleza_s2.dto.UsuarioRequestDTO;
import com.phabloraylan.beleza_s2.dto.UsuarioResponseDTO;
import com.phabloraylan.beleza_s2.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioResponseDTO usuarioResponseDTO;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
        usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(1L);
        usuarioResponseDTO.setNome("João Silva");
        usuarioResponseDTO.setEmail("joao@example.com");
        usuarioResponseDTO.setTelefone("11999999999");

        usuarioRequestDTO = new UsuarioRequestDTO();
        usuarioRequestDTO.setNome("João Silva");
        usuarioRequestDTO.setEmail("joao@example.com");
        usuarioRequestDTO.setTelefone("11999999999");
    }

    @Test
    @WithMockUser
    @DisplayName("Deve listar todos os usuários")
    void findAll_ShouldReturnListOfUsuarios() throws Exception {
        when(usuarioService.findAll()).thenReturn(Collections.singletonList(usuarioResponseDTO));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(usuarioResponseDTO.getId()))
                .andExpect(jsonPath("$[0].nome").value(usuarioResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar usuário por ID")
    void findById_ShouldReturnUsuario() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(get("/api/usuarios/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(usuarioResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar novo usuário")
    void create_ShouldReturnCreatedUsuario() throws Exception {
        when(usuarioService.save(any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(usuarioResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(usuarioResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar usuário")
    void update_ShouldReturnUpdatedUsuario() throws Exception {
        when(usuarioService.update(eq(1L), any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseDTO);

        mockMvc.perform(put("/api/usuarios/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(usuarioResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve deletar usuário")
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(usuarioService).delete(1L);

        mockMvc.perform(delete("/api/usuarios/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
