package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.application.dto.PontoEletronicoDto;
import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.business.entities.PontoEletronicoEntity;
import com.postechhackaton.relatorios.domain.usecase.FormataRelatorioPeriodoUseCase;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class FormataRelatorioPeriodoUseCaseImpl implements FormataRelatorioPeriodoUseCase {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String formatar(RelatorioPeriodoPontoDto relatorio) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2>Relatório de Ponto</h2>");
        content.append("<div>");
        content.append("<b>Funcionário: </b>").append("<span>").append(relatorio.getUsuario()).append("</span>").append("<br/>");
        content.append("<b>Total de horas trabalhadas: </b>").append("<span>").append(relatorio.getTotalHorasTrabalhadas()).append("</span>").append("<br/>");
        content.append("<b>Início: </b>").append("<span>").append(relatorio.getDataInicio().format(DATE_TIME_FORMATTER)).append("</span>").append("<br/>");
        content.append("<b>Fim: </b>").append("<span>").append(relatorio.getDataFim().format(DATE_TIME_FORMATTER)).append("</span>").append("<br/>");
        content.append("</div>");
        content.append("<h2>Espelho ponto:</h2>");
        for (var pontoDia : relatorio.getRegistros()) {
            content.append("<div>").append("<b>Status: </b>").append("<span>").append(pontoDia.getStatus()).append("</span>").append("<br/>");
            content.append("<b>Data: </b>").append("<span>").append(pontoDia.getData().format(DATE_TIME_FORMATTER)).append("</span>").append("<br/>");
            content.append("<b>Total de horas: </b>").append("<span>").append(pontoDia.getTotalHorasTrabalhadas()).append("</span>").append("<br/>");
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
}
