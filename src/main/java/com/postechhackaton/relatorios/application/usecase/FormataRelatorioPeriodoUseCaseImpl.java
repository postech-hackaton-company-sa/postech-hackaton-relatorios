package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.business.entities.FuncionarioEntity;
import com.postechhackaton.relatorios.domain.usecase.FormataRelatorioPeriodoUseCase;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class FormataRelatorioPeriodoUseCaseImpl implements FormataRelatorioPeriodoUseCase {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String formatar(RelatorioPeriodoPontoDto relatorio, FuncionarioEntity funcionario) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h1>Relatório de Ponto</h1>");
        content.append("<div>");
        content.append("<b>Funcionário: </b>").append("<br />");
        if (funcionario != null) {
            adicionaDadoCasoExista("Username", funcionario.getUsername(), content);
            adicionaDadoCasoExista("Nome", funcionario.getNome(), content);
            adicionaDadoCasoExista("Sobrenome", funcionario.getSobrenome(), content);
            adicionaDadoCasoExista("CPF", funcionario.getCpf(), content);
            adicionaDadoCasoExista("E-mail", funcionario.getEmail(), content);
        }
        content.append("<b>Início: </b>").append("<span>").append(relatorio.getDataInicio().format(DATE_TIME_FORMATTER)).append("</span>").append("<br/>");
        content.append("<b>Fim: </b>").append("<span>").append(relatorio.getDataFim().format(DATE_TIME_FORMATTER)).append("</span>").append("<br/>");
        content.append("<b>Total de horas trabalhadas: </b>").append("<span>").append(relatorio.getTotalHorasTrabalhadas()).append("</span>").append("<br/>");
        content.append("</div>");
        content.append("<h2>Espelho ponto:</h2>");
        for (var pontoDia : relatorio.getRegistros()) {
            content.append("<b>Data: </b>").append("<span>").append(pontoDia.getData().format(DATE_TIME_FORMATTER)).append("</span>").append("<br/>");
            content.append("<b>Total de horas: </b>").append("<span>").append(pontoDia.getTotalHorasTrabalhadas()).append("</span>").append("<br/>");
            content.append("<div>").append("<b>Status: </b>").append("<span>").append(pontoDia.getStatus()).append("</span>").append("<br/>");
            content.append("<ul>");
            for (var registroDia : pontoDia.getRegistros()) {
                content.append("<li>").append(registroDia.getData().toLocalTime()).append(" (").append(registroDia.getTipo()).append(")</li>");
            }
            content.append("</ul>");
            content.append("</div>");
        }
        content.append("</body></html>");
        return content.toString();
    }

    private void adicionaDadoCasoExista(String titulo, String dado, StringBuilder content) {
        if (dado != null && !dado.isBlank()) {
            content.append("<b>").append(titulo).append(":</b> <span>").append("<span>").append(dado).append("</span>").append("<br>");
        }
    }
}
