package com.postechhackaton.relatorios.application.controller;

import com.postechhackaton.relatorios.application.dto.ExceptionResponse;
import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.application.dto.QueryEspelhoPontoDto;
import com.postechhackaton.relatorios.domain.usecase.RequisitarRegistrosEspelhoPontoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/v1/relatorios")
public class RelatoriosController {

    private final RequisitarRegistrosEspelhoPontoUseCase requisitarRegistrosEspelhoPontoUseCase;

    public RelatoriosController(RequisitarRegistrosEspelhoPontoUseCase requisitarRegistrosEspelhoPontoUseCase) {
        this.requisitarRegistrosEspelhoPontoUseCase = requisitarRegistrosEspelhoPontoUseCase;
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void criarRelatorio(@NotBlank @RequestHeader("username") @Schema(description = "Usuario que esta fazendo a requisicao") String usuarioRequerinte,
                                @RequestHeader("roles") @Pattern(regexp = ".*admin.*", message = "Apenas administradores podem requisitar relatorio de funcionarios") @Schema(description = "Roles do usuario que esta fazendo a requisicao") String roles,
                                @NotBlank(message = "E-mail deve ser informado") @Schema(description = "Email que será enviado o relatório") @RequestParam("email") String email,
                                @NotBlank(message = "Usuario do relatorio deve ser informado") @RequestParam("usuario") @Schema(description = "Usuário que será realizado o relatório") String usuario,
                                @NotNull(message = "Data inicial do relatorio precisa ser informada") @RequestParam("dataInicio") @Schema(example = "2024-03-01T00:00:00") LocalDateTime dataInicio,
                                @NotNull(message = "Data encerramento do relatorio precisa ser informada") @RequestParam("dataFim") @Schema(example = "2024-03-03T00:00:00") LocalDateTime dataFim) throws Exception {
        QueryEspelhoPontoDto query = QueryEspelhoPontoDto.builder().dataInicio(dataInicio).dataFim(dataFim).usuarioRequerinte(usuarioRequerinte).usuario(usuario).email(email).build();
        requisitarRegistrosEspelhoPontoUseCase.requisitar(query);
    }

}
