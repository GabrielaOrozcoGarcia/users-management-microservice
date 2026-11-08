package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.CalificacionCommand;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;

public interface UpdateCalificacionUseCase {
  CalificacionModel update(Long id, CalificacionCommand command);
}
