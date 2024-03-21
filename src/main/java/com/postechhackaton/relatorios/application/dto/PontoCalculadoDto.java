package com.postechhackaton.relatorios.application.dto;

import com.postechhackaton.relatorios.business.entities.PontoEletronicoEntity;
import com.postechhackaton.relatorios.business.enums.StatusPonto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PontoCalculadoDto {
    private StatusPonto status;

    private LocalDate data;

    private List<PontoEletronicoEntity> registros;

    private long totalHorasTrabalhadas;
}
