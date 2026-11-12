package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.CalificacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@DisplayName("CalificacionRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CalificacionRepositoryMySQLTest {

  @Mock private DataSource dataSource;
  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;
  @Mock private ResultSet generatedKeys;

  private CalificacionRepositoryMySQL repository;

  private final CalificacionModel calificacion = new CalificacionModel(
      7L, LocalDate.of(2026, 11, 8), "Ana", "John", "Electiva II", "Sistemas", "Unicolombo",
      "2026-2", "Parcial", new BigDecimal("30"), new BigDecimal("4.5"));

  @BeforeEach
  void setUp() throws SQLException {
    repository = new CalificacionRepositoryMySQL(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
    when(statement.getGeneratedKeys()).thenReturn(generatedKeys);
  }

  private void rowWith(final long id) throws SQLException {
    when(resultSet.getLong("id")).thenReturn(id);
    when(resultSet.getDate("fecha")).thenReturn(Date.valueOf("2026-11-08"));
    when(resultSet.getString("estudiante")).thenReturn("Ana");
    when(resultSet.getString("docente")).thenReturn("John");
    when(resultSet.getString("asignatura")).thenReturn("Electiva II");
    when(resultSet.getString("carrera")).thenReturn("Sistemas");
    when(resultSet.getString("universidad")).thenReturn("Unicolombo");
    when(resultSet.getString("periodo")).thenReturn("2026-2");
    when(resultSet.getString("actividad_evaluada")).thenReturn("Parcial");
    when(resultSet.getBigDecimal("porcentaje")).thenReturn(new BigDecimal("30"));
    when(resultSet.getBigDecimal("nota")).thenReturn(new BigDecimal("4.5"));
  }

  @Test
  void saveInsertsAndReturnsRowWithGeneratedId() throws SQLException {
    when(generatedKeys.next()).thenReturn(true);
    when(generatedKeys.getLong(1)).thenReturn(7L);
    when(resultSet.next()).thenReturn(true);
    rowWith(7L);

    final CalificacionModel saved = repository.save(calificacion.withId(null));

    assertThat(saved.id()).isEqualTo(7L);
    assertThat(saved.actividadEvaluada()).isEqualTo("Parcial");
    verify(statement).setString(8, "Parcial");
    verify(statement).setBigDecimal(10, new BigDecimal("4.5"));
  }

  @Test
  void saveWrapsSqlException() throws SQLException {
    when(statement.executeUpdate()).thenThrow(new SQLException("boom"));
    assertThatThrownBy(() -> repository.save(calificacion))
        .isInstanceOf(PersistenceException.class);
  }

  @Test
  void updateReturnsUpdatedRow() throws SQLException {
    when(resultSet.next()).thenReturn(true);
    rowWith(7L);
    assertThat(repository.update(calificacion).id()).isEqualTo(7L);
    verify(statement).setLong(11, 7L);
  }

  @Test
  void updateFailsWhenRowDisappears() throws SQLException {
    when(resultSet.next()).thenReturn(false);
    assertThatThrownBy(() -> repository.update(calificacion))
        .isInstanceOf(CalificacionNotFoundException.class);
  }

  @Test
  void updateWrapsSqlException() throws SQLException {
    when(statement.executeUpdate()).thenThrow(new SQLException("boom"));
    assertThatThrownBy(() -> repository.update(calificacion))
        .isInstanceOf(PersistenceException.class);
  }

  @Test
  void findByIdFoundAndNotFound() throws SQLException {
    when(resultSet.next()).thenReturn(true, false);
    rowWith(7L);
    assertThat(repository.findById(7L)).isPresent();
    assertThat(repository.findById(8L)).isEmpty();
  }

  @Test
  void findByIdWrapsSqlException() throws SQLException {
    when(statement.executeQuery()).thenThrow(new SQLException("boom"));
    assertThatThrownBy(() -> repository.findById(1L)).isInstanceOf(PersistenceException.class);
  }

  @Test
  void findAllReturnsEveryRow() throws SQLException {
    when(resultSet.next()).thenReturn(true, true, false);
    rowWith(7L);
    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void findAllWrapsSqlException() throws SQLException {
    when(statement.executeQuery()).thenThrow(new SQLException("boom"));
    assertThatThrownBy(() -> repository.findAll()).isInstanceOf(PersistenceException.class);
  }

  @Test
  void deleteExecutesStatement() throws SQLException {
    repository.deleteById(7L);
    verify(statement).setLong(1, 7L);
    verify(statement).executeUpdate();
  }

  @Test
  void deleteWrapsSqlException() throws SQLException {
    when(statement.executeUpdate()).thenThrow(new SQLException("boom"));
    assertThatThrownBy(() -> repository.deleteById(7L)).isInstanceOf(PersistenceException.class);
  }
}
