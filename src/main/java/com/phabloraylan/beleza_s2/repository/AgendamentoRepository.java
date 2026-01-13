package com.phabloraylan.beleza_s2.repository;

import com.phabloraylan.beleza_s2.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByEstabelecimentoId(Long estabelecimentoId);
    List<Agendamento> findByProfissionalId(Long profissionalId);
    List<Agendamento> findByUsuarioId(Long usuarioId);
}
