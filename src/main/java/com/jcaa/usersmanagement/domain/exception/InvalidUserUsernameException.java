package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserUsernameException extends DomainException {

  private static final String MESSAGE_EMPTY = "El username no puede estar vacio.";
  private static final String MESSAGE_FORMAT =
      "El username '%s' no es valido: use de 3 a 50 letras, numeros, punto, guion o guion bajo.";

  private InvalidUserUsernameException(final String message) {
    super(message);
  }

  public static InvalidUserUsernameException becauseValueIsEmpty() {
    return new InvalidUserUsernameException(MESSAGE_EMPTY);
  }

  public static InvalidUserUsernameException becauseFormatIsInvalid(final String value) {
    return new InvalidUserUsernameException(String.format(MESSAGE_FORMAT, value));
  }
}
