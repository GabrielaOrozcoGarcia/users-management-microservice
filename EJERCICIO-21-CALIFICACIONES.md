# Ejercicio 21 – Calificaciones

Tablas del ejercicio:

| Tabla | Campos |
|-------|--------|
| `users` (Usuario) | id, **username**, name (nombre), email, password, role, status |
| `calificaciones` | id, fecha, estudiante, docente, asignatura, carrera, universidad, periodo, actividad_evaluada, porcentaje, nota |

El script está en `src/main/resources/schema.sql` (ejecútelo completo en TiDB).
Si ya había creado la tabla `users` sin `username`, ejecute además
`src/main/resources/migration-001-username.sql`.

## Endpoints

Los de calificaciones y usuarios (salvo el registro) requieren el header
`Authorization: Bearer <accessToken>`, que se obtiene con el login.

| Método | URL | Body |
|--------|-----|------|
| POST | `/api/auth/login` (sin token) | `{"username":"admin","password":"Admin1234!"}` (también acepta `email`) |
| POST | `/api/auth/recover` (sin token) | `{"email":"usuario@example.com"}` → envía contraseña temporal con Brevo |
| POST | `/api/users` (sin token, registro) | `{"username":"gorozco","name":"Gabriela","email":"g@x.com","password":"Clave12345"}` |
| GET / PUT / DELETE | `/api/users[/{id}]` | solo rol ADMIN (GET también REVIEWER) |
| GET | `/api/calificaciones` | – |
| GET | `/api/calificaciones/{id}` | – |
| POST | `/api/calificaciones` | JSON de abajo |
| PUT | `/api/calificaciones/{id}` | JSON de abajo |
| DELETE | `/api/calificaciones/{id}` | – |

```json
{
  "fecha": "2026-11-08",
  "estudiante": "Ana Perez",
  "docente": "John Arrieta",
  "asignatura": "Electiva II",
  "carrera": "Ingenieria de Sistemas",
  "universidad": "Unicolombo",
  "periodo": "2026-2",
  "actividadEvaluada": "Parcial 1",
  "porcentaje": 30,
  "nota": 4.5
}
```

Reglas: `fecha` en formato `yyyy-MM-dd`, `porcentaje` entre 0 y 100, `nota` entre 0.0 y 5.0.

Respuestas: 200 OK, 201 Created, 204 No Content (DELETE), 400 datos inválidos,
401 sin token o token vencido (el JWT dura 15 min), 404 id inexistente.

## App Kodular

`AppCalificaciones_API.aia` ya consume este API (antes usaba Supabase).
Lo único que hay que cambiar es la variable global `API_URL` en los bloques de **Screen1**
(poner la URL de Render). Las demás pantallas la leen desde TinyDB.

Flujo: registrarse → el ADMIN entra a "Gestión de Usuarios", selecciona al usuario, marca
"Usuario activo" y pulsa Actualizar → el usuario ya puede iniciar sesión.

Render plan Free se duerme tras 15 min sin uso: la primera petición puede tardar ~50 s.
