package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.CalificacionModel;

public interface GetCalificacionByIdUseCase {
  CalificacionModel getById(Long id);
}
