package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.CalificacionRepositoryPort;
import com.jcaa.usersmanagement.domain.exception.CalificacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: persiste calificaciones en MySQL / TiDB con JDBC puro. */
@Repository
@RequiredArgsConstructor
public class CalificacionRepositoryMySQL implements CalificacionRepositoryPort {

  private static final String COLUMNS =
      "id, fecha, estudiante, docente, asignatura, carrera, universidad, periodo, "
          + "actividad_evaluada, porcentaje, nota";

  private static final String SQL_INSERT =
      "INSERT INTO calificaciones (fecha, estudiante, docente, asignatura, carrera, universidad, "
          + "periodo, actividad_evaluada, porcentaje, nota) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String SQL_UPDATE =
      "UPDATE calificaciones SET fecha = ?, estudiante = ?, docente = ?, asignatura = ?, "
          + "carrera = ?, universidad = ?, periodo = ?, actividad_evaluada = ?, porcentaje = ?, "
          + "nota = ? WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT " + COLUMNS + " FROM calificaciones WHERE id = ?";

  private static final String SQL_SELECT_ALL =
      "SELECT " + COLUMNS + " FROM calificaciones ORDER BY fecha DESC, id DESC";

  private static final String SQL_DELETE = "DELETE FROM calificaciones WHERE id = ?";

  private final DataSource dataSource;

  @Override
  public CalificacionModel save(final CalificacionModel calificacion) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
      bindFields(statement, calificacion);
      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        keys.next();
        return findByIdOrFail(keys.getLong(1));
      }
    } catch (final SQLException exception) {
      throw PersistenceException.becauseSaveFailed("calificacion nueva", exception);
    }
  }

  @Override
  public CalificacionModel update(final CalificacionModel calificacion) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      bindFields(statement, calificacion);
      statement.setLong(11, calificacion.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseUpdateFailed(String.valueOf(calificacion.id()), exception);
    }
    return findByIdOrFail(calificacion.id());
  }

  @Override
  public Optional<CalificacionModel> findById(final Long id) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next() ? Optional.of(toModel(resultSet)) : Optional.empty();
      }
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindByIdFailed(String.valueOf(id), exception);
    }
  }

  @Override
  public List<CalificacionModel> findAll() {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
        ResultSet resultSet = statement.executeQuery()) {
      final List<CalificacionModel> result = new ArrayList<>();
      while (resultSet.next()) {
        result.add(toModel(resultSet));
      }
      return result;
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindAllFailed(exception);
    }
  }

  @Override
  public void deleteById(final Long id) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setLong(1, id);
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseDeleteFailed(String.valueOf(id), exception);
    }
  }

  private CalificacionModel findByIdOrFail(final Long id) {
    return findById(id).orElseThrow(() -> CalificacionNotFoundException.becauseIdWasNotFound(id));
  }

  private static void bindFields(final PreparedStatement statement, final CalificacionModel c)
      throws SQLException {
    statement.setDate(1, Date.valueOf(c.fecha()));
    statement.setString(2, c.estudiante());
    statement.setString(3, c.docente());
    statement.setString(4, c.asignatura());
    statement.setString(5, c.carrera());
    statement.setString(6, c.universidad());
    statement.setString(7, c.periodo());
    statement.setString(8, c.actividadEvaluada());
    statement.setBigDecimal(9, c.porcentaje());
    statement.setBigDecimal(10, c.nota());
  }

  private static CalificacionModel toModel(final ResultSet rs) throws SQLException {
    return new CalificacionModel(
        rs.getLong("id"),
        rs.getDate("fecha").toLocalDate(),
        rs.getString("estudiante"),
        rs.getString("docente"),
        rs.getString("asignatura"),
        rs.getString("carrera"),
        rs.getString("universidad"),
        rs.getString("periodo"),
        rs.getString("actividad_evaluada"),
        rs.getBigDecimal("porcentaje"),
        rs.getBigDecimal("nota"));
  }
}
