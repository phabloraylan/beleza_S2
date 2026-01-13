package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.dto.AgendamentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.AgendamentoResponseDTO;
import com.phabloraylan.beleza_s2.entity.Agendamento;
import com.phabloraylan.beleza_s2.entity.Estabelecimento;
import com.phabloraylan.beleza_s2.entity.Profissional;
import com.phabloraylan.beleza_s2.entity.StatusAgendamento;
import com.phabloraylan.beleza_s2.entity.Usuario;
import com.phabloraylan.beleza_s2.repository.AgendamentoRepository;
import com.phabloraylan.beleza_s2.repository.EstabelecimentoRepository;
import com.phabloraylan.beleza_s2.repository.ProfissionalRepository;
import com.phabloraylan.beleza_s2.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProfissionalRepository profissionalRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final NotificacaoService notificacaoService;
    private final IntegracaoCalendarioService integracaoCalendarioService;

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> findAll() {
        return agendamentoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AgendamentoResponseDTO findById(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com id: " + id));
        return toResponseDTO(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> findByEstabelecimentoId(Long estabelecimentoId) {
        return agendamentoRepository.findByEstabelecimentoId(estabelecimentoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AgendamentoResponseDTO save(AgendamentoRequestDTO agendamentoDTO) {
        Usuario usuario = usuarioRepository.findById(agendamentoDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Profissional profissional = profissionalRepository.findById(agendamentoDTO.getProfissionalId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(agendamentoDTO.getEstabelecimentoId())
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado"));

        Agendamento agendamento = new Agendamento();
        agendamento.setUsuario(usuario);
        agendamento.setProfissional(profissional);
        agendamento.setEstabelecimento(estabelecimento);
        agendamento.setDataHora(agendamentoDTO.getDataHora());
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        Agendamento savedAgendamento = agendamentoRepository.save(agendamento);
        notificacaoService.enviarConfirmacaoAgendamento(savedAgendamento);
        integracaoCalendarioService.sincronizarCalendario(savedAgendamento);
        return toResponseDTO(savedAgendamento);
    }

    @Transactional
    public AgendamentoResponseDTO update(Long id, AgendamentoRequestDTO agendamentoDTO) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com id: " + id));

        Usuario usuario = usuarioRepository.findById(agendamentoDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Profissional profissional = profissionalRepository.findById(agendamentoDTO.getProfissionalId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(agendamentoDTO.getEstabelecimentoId())
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado"));

        agendamento.setUsuario(usuario);
        agendamento.setProfissional(profissional);
        agendamento.setEstabelecimento(estabelecimento);
        agendamento.setDataHora(agendamentoDTO.getDataHora());
        
        if (agendamentoDTO.getStatus() != null) {
            agendamento.setStatus(agendamentoDTO.getStatus());
        }
        
        Agendamento updatedAgendamento = agendamentoRepository.save(agendamento);
        
        if (updatedAgendamento.getStatus() == StatusAgendamento.CANCELADO) {
            integracaoCalendarioService.removerDoCalendario(updatedAgendamento);
        } else {
            integracaoCalendarioService.sincronizarCalendario(updatedAgendamento);
        }
        
        return toResponseDTO(updatedAgendamento);
    }

    @Transactional
    public void delete(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com id: " + id));
        integracaoCalendarioService.removerDoCalendario(agendamento);
        agendamentoRepository.delete(agendamento);
    }

    private AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();
        dto.setId(agendamento.getId());
        dto.setUsuarioId(agendamento.getUsuario().getId());
        dto.setUsuarioNome(agendamento.getUsuario().getNome());
        dto.setProfissionalId(agendamento.getProfissional().getId());
        dto.setProfissionalNome(agendamento.getProfissional().getNome());
        dto.setEstabelecimentoId(agendamento.getEstabelecimento().getId());
        dto.setEstabelecimentoNome(agendamento.getEstabelecimento().getNome());
        dto.setDataHora(agendamento.getDataHora());
        dto.setStatus(agendamento.getStatus());
        dto.setDataCriacao(agendamento.getDataCriacao());
        dto.setDataAtualizacao(agendamento.getDataAtualizacao());
        return dto;
    }
}
