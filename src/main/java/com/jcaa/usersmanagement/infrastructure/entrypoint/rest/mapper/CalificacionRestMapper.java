package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CalificacionCommand;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CalificacionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CalificacionRestResponse;
import java.util.List;

public final class CalificacionRestMapper {

  private CalificacionRestMapper() {}

  public static CalificacionCommand toCommand(final CalificacionRestRequest r) {
    return new CalificacionCommand(
        r.fecha(), r.estudiante(), r.docente(), r.asignatura(), r.carrera(), r.universidad(),
        r.periodo(), r.actividadEvaluada(), r.porcentaje(), r.nota());
  }

  public static CalificacionRestResponse toResponse(final CalificacionModel m) {
    return new CalificacionRestResponse(
        m.id(), m.fecha(), m.estudiante(), m.docente(), m.asignatura(), m.carrera(),
        m.universidad(), m.periodo(), m.actividadEvaluada(), m.porcentaje(), m.nota());
  }

  public static List<CalificacionRestResponse> toResponseList(final List<CalificacionModel> list) {
    return list.stream().map(CalificacionRestMapper::toResponse).toList();
  }
}
