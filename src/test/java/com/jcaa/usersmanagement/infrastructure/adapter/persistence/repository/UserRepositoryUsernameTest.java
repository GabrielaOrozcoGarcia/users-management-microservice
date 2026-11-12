package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.valueobject.UserUsername;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserRepositoryUsernameTest {

  @Mock private DataSource dataSource;
  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;

  private UserRepositoryMySQL repository;

  @BeforeEach
  void setUp() throws SQLException {
    repository = new UserRepositoryMySQL(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
  }

  @Test
  void findsUserByUsername() throws SQLException {
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("id")).thenReturn("u-1");
    when(resultSet.getString("username")).thenReturn("gorozco");
    when(resultSet.getString("name")).thenReturn("Gabriela Orozco");
    when(resultSet.getString("email")).thenReturn("g@example.com");
    when(resultSet.getString("password")).thenReturn("$2a$12$hash");
    when(resultSet.getString("role")).thenReturn("MEMBER");
    when(resultSet.getString("status")).thenReturn("ACTIVE");

    assertThat(repository.getByUsername(new UserUsername("gorozco")))
        .hasValueSatisfying(u -> assertThat(u.getUsername().value()).isEqualTo("gorozco"));
  }

  @Test
  void returnsEmptyWhenUsernameDoesNotExist() throws SQLException {
    when(resultSet.next()).thenReturn(false);
    assertThat(repository.getByUsername(new UserUsername("nadie"))).isEmpty();
  }

  @Test
  void wrapsSqlException() throws SQLException {
    when(statement.executeQuery()).thenThrow(new SQLException("boom"));
    assertThatThrownBy(() -> repository.getByUsername(new UserUsername("x_y")))
        .isInstanceOf(PersistenceException.class);
  }
}
