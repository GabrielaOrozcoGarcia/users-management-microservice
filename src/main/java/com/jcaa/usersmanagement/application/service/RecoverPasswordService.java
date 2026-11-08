package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.RecoverPasswordUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.domain.exception.DomainException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import java.security.SecureRandom;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecoverPasswordService implements RecoverPasswordUseCase {

  private static final String ALPHABET =
      "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
  private static final int TEMP_PASSWORD_LENGTH = 12;
  private static final SecureRandom RANDOM = new SecureRandom();

  private final GetUserByEmailPort getUserByEmailPort;
  private final UpdateUserPort updateUserPort;
  private final EmailNotificationService emailNotificationService;

  @Override
  public void execute(final String email) {
    findUser(email)
        .ifPresentOrElse(
            this::resetPassword,
            () -> log.info("[RecoverPasswordService] solicitud para un email no registrado."));
  }

  private Optional<UserModel> findUser(final String email) {
    try {
      return getUserByEmailPort.getByEmail(new UserEmail(email));
    } catch (final DomainException invalidEmail) {
      return Optional.empty();
    }
  }

  private void resetPassword(final UserModel user) {
    final String temporaryPassword = generateTemporaryPassword();
    final UserModel updated =
        updateUserPort.update(user.withPassword(UserPassword.fromPlainText(temporaryPassword)));
    emailNotificationService.notifyPasswordReset(updated, temporaryPassword);
  }

  static String generateTemporaryPassword() {
    final StringBuilder builder = new StringBuilder(TEMP_PASSWORD_LENGTH);
    for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
      builder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
    }
    return builder.toString();
  }
}
