package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Login con {@code email} o con {@code username} (basta con enviar uno de los dos). */
public record LoginRestRequest(
    @Schema(example = "admin@example.com") String email,
    @Schema(example = "admin") String username,
    @NotBlank @Size(min = 8) String password) {

  public LoginRestRequest(final String email, final String password) {
    this(email, null, password);
  }

  /** Identificador usado para autenticar: el email si viene, si no el username. */
  @JsonIgnore
  public String identifier() {
    return email != null && !email.isBlank() ? email : username;
  }

  @JsonIgnore
  @AssertTrue(message = "email or username must not be blank")
  public boolean isIdentifierPresent() {
    final String value = identifier();
    return value != null && !value.isBlank();
  }
}
