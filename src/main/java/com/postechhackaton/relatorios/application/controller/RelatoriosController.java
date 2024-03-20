package com.postechhackaton.relatorios.application.controller;

import com.postechhackaton.relatorios.application.dto.ExceptionResponse;
import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoPeriodoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/v1/relatorios")
public class RelatoriosController {

    private final CalcularPontoPeriodoUseCase calcularPontoPeriodoUseCase;

    public RelatoriosController(CalcularPontoPeriodoUseCase calcularPontoPeriodoUseCase) {
        this.calcularPontoPeriodoUseCase = calcularPontoPeriodoUseCase;
    }

    @GetMapping
    @Operation(
            summary = "Calcula o ponto de um usuario",
            description = "Calcula o ponto de um usuario baseado nas datas informadas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Ponto calculado com sucesso",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PontoCalculadoDto.class)
                            )
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Parametros invalidos para calcular o ponto",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)
                            )
                    }),
    })
    public RelatorioPeriodoPontoDto criarRelatorio(@RequestHeader("usuario") String usuario,
                                                  @NotNull(message = "Data inicial do relatorio precisa ser informada") @RequestParam("dataInicio") LocalDate dataInicio,
                                                  @NotNull(message = "Data encerramento do relatorio precisa ser informada") @RequestParam("dataFim") LocalDate dataFim) {
        return calcularPontoPeriodoUseCase.execute(usuario, dataInicio, dataFim);
    }

}
