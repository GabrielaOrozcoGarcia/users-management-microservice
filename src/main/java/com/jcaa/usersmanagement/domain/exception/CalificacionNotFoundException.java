package com.jcaa.usersmanagement.domain.exception;

public final class CalificacionNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "La calificacion con id '%s' no existe.";

  private CalificacionNotFoundException(final String message) {
    super(message);
  }

  public static CalificacionNotFoundException becauseIdWasNotFound(final Long id) {
    return new CalificacionNotFoundException(String.format(MESSAGE_BY_ID, id));
  }
}
