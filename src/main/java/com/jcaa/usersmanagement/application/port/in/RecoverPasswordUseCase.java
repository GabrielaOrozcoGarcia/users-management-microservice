package com.jcaa.usersmanagement.application.port.in;

public interface RecoverPasswordUseCase {

  /**
   * Genera una contraseña temporal para el usuario con ese email y se la envia por correo. Si el
   * email no existe no hace nada (para no revelar que correos estan registrados).
   */
  void execute(String email);
}
