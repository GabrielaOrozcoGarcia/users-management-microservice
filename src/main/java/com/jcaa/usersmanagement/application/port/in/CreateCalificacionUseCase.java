package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.CalificacionCommand;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;

public interface CreateCalificacionUseCase {
  CalificacionModel create(CalificacionCommand command);
}
