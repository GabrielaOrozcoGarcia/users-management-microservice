package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

public record UserRestResponse(
    String id,
    String name,
    String email,
    String role,
    String status,
    String username) {

  public UserRestResponse(
      final String id, final String name, final String email, final String role,
      final String status) {
    this(id, name, email, role, status, null);
  }
}
