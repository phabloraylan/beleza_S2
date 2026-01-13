package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.UsuarioRequestDTO;
import com.phabloraylan.beleza_s2.dto.UsuarioResponseDTO;
import com.phabloraylan.beleza_s2.entity.Usuario;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setTelefone("11999999999");
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());

        usuarioRequestDTO = new UsuarioRequestDTO();
        usuarioRequestDTO.setNome("João Silva");
        usuarioRequestDTO.setEmail("joao@example.com");
        usuarioRequestDTO.setTelefone("11999999999");
    }

    @Test
    @DisplayName("Deve retornar lista de usuários")
    void findAll_ShouldReturnListOfUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

        List<UsuarioResponseDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(usuario.getNome(), result.get(0).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar usuário por ID")
    void findById_ShouldReturnUsuario_WhenIdExists() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getNome(), result.getNome());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID não existe")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.findById(1L));
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve salvar novo usuário")
    void save_ShouldReturnSavedUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO result = usuarioService.save(usuarioRequestDTO);

        assertNotNull(result);
        assertEquals(usuario.getNome(), result.getNome());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário existente")
    void update_ShouldReturnUpdatedUsuario_WhenIdExists() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO result = usuarioService.update(1L, usuarioRequestDTO);

        assertNotNull(result);
        assertEquals(usuario.getNome(), result.getNome());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ID inexistente")
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.update(1L, usuarioRequestDTO));
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve deletar usuário")
    void delete_ShouldDeleteUsuario_WhenIdExists() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.delete(1L);

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ID inexistente")
    void delete_ShouldThrowException_WhenIdDoesNotExist() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.delete(1L));
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
}
