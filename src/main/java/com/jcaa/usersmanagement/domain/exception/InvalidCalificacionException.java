package com.jcaa.usersmanagement.domain.exception;

import java.math.BigDecimal;

public final class InvalidCalificacionException extends DomainException {

  private static final String MESSAGE_REQUIRED = "El campo '%s' de la calificacion es obligatorio.";
  private static final String MESSAGE_RANGE =
      "El campo '%s' de la calificacion debe estar entre %s y %s.";

  private InvalidCalificacionException(final String message) {
    super(message);
  }

  public static InvalidCalificacionException becauseFieldIsRequired(final String field) {
    return new InvalidCalificacionException(String.format(MESSAGE_REQUIRED, field));
  }

  public static InvalidCalificacionException becauseOutOfRange(
      final String field, final BigDecimal min, final BigDecimal max) {
    return new InvalidCalificacionException(
        String.format(MESSAGE_RANGE, field, min.toPlainString(), max.toPlainString()));
  }
}
