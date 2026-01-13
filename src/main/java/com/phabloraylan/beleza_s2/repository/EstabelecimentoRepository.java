package com.phabloraylan.beleza_s2.repository;

import com.phabloraylan.beleza_s2.entity.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {

    @Query("SELECT DISTINCT e FROM Estabelecimento e " +
            "LEFT JOIN e.servicos s " +
            "LEFT JOIN e.profissionais p " +
            "WHERE " +
            "(:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:endereco IS NULL OR LOWER(e.endereco) LIKE LOWER(CONCAT('%', :endereco, '%'))) AND " +
            "(:servico IS NULL OR LOWER(s) LIKE LOWER(CONCAT('%', :servico, '%'))) AND " +
            "(:precoMinimo IS NULL OR p.tarifa >= :precoMinimo) AND " +
            "(:precoMaximo IS NULL OR p.tarifa <= :precoMaximo)")
    List<Estabelecimento> search(
            @Param("nome") String nome,
            @Param("endereco") String endereco,
            @Param("servico") String servico,
            @Param("precoMinimo") Double precoMinimo,
            @Param("precoMaximo") Double precoMaximo
    );
}
