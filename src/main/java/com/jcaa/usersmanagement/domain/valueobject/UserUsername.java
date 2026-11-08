package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserUsernameException;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Nombre de usuario (username) para iniciar sesion. Se normaliza a minusculas y solo admite letras,
 * numeros, punto, guion y guion bajo (3 a 50 caracteres).
 */
public record UserUsername(String value) {

  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9._-]{3,50}$");

  public UserUsername {
    final String normalized =
        Objects.requireNonNull(value, "UserUsername cannot be null").trim().toLowerCase(Locale.ROOT);
    if (normalized.isEmpty()) {
      throw InvalidUserUsernameException.becauseValueIsEmpty();
    }
    if (!USERNAME_PATTERN.matcher(normalized).matches()) {
      throw InvalidUserUsernameException.becauseFormatIsInvalid(normalized);
    }
    value = normalized;
  }

  /** Username por defecto cuando no se envia uno: la parte del email antes de la arroba. */
  public static UserUsername fromEmail(final UserEmail email) {
    String local = email.value().substring(0, email.value().indexOf('@'));
    local = local.replaceAll("[^a-z0-9._-]", "");
    while (local.length() < 3) {
      local = local + "_";
    }
    return new UserUsername(local.length() > 50 ? local.substring(0, 50) : local);
  }

  /** Crea el username a partir del texto recibido o, si viene vacio, a partir del email. */
  public static UserUsername ofNullable(final String rawUsername, final UserEmail email) {
    return Objects.isNull(rawUsername) || rawUsername.isBlank()
        ? fromEmail(email)
        : new UserUsername(rawUsername);
  }

  @Override
  public String toString() {
    return value;
  }
}
