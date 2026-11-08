package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

public record UserEntity(
    String id,
    String name,
    String email,
    String password,
    String role,
    String status,
    String createdAt,
    String updatedAt,
    String username) {

  public UserEntity(
      final String id, final String name, final String email, final String password,
      final String role, final String status, final String createdAt, final String updatedAt) {
    this(id, name, email, password, role, status, createdAt, updatedAt, null);
  }
}
