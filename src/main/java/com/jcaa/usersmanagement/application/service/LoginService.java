package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByUsernamePort;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.exception.DomainException;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserUsername;
import java.util.Optional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

  private final GetUserByEmailPort getUserByEmailPort;
  private final Validator validator;
  private final GetUserByUsernamePort getUserByUsernamePort;

  @Override
  public UserModel execute(final LoginCommand command) {
    validateCommand(command);

    final UserModel user = findUserOrFailWithInvalidCredentials(command.email());

    verifyPasswordOrFail(command.password(), user);
    ensureUserIsActiveOrFail(user);

    return user;
  }

  private void validateCommand(final LoginCommand command) {
    final Set<ConstraintViolation<LoginCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  /** Busca por email si el identificador contiene '@'; si no, lo busca como username. */
  private UserModel findUserOrFailWithInvalidCredentials(final String emailOrUsername) {
    return findUser(emailOrUsername)
        .orElseThrow(InvalidCredentialsException::becauseCredentialsAreInvalid);
  }

  private Optional<UserModel> findUser(final String emailOrUsername) {
    try {
      if (emailOrUsername.contains("@")) {
        return getUserByEmailPort.getByEmail(new UserEmail(emailOrUsername));
      }
      return getUserByUsernamePort.getByUsername(new UserUsername(emailOrUsername));
    } catch (final DomainException invalidFormat) {
      return Optional.empty();
    }
  }

  private static void verifyPasswordOrFail(final String plainPassword, final UserModel user) {
    if (!user.getPassword().verifyPlain(plainPassword)) {
      throw InvalidCredentialsException.becauseCredentialsAreInvalid();
    }
  }

  private static void ensureUserIsActiveOrFail(final UserModel user) {
    if (user.getStatus() != UserStatus.ACTIVE) {
      throw InvalidCredentialsException.becauseUserIsNotActive();
    }
  }
}
