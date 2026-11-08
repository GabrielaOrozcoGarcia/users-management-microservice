# Ejercicio 21 – Calificaciones

Tablas del ejercicio:

| Tabla | Campos |
|-------|--------|
| `users` (Usuario) | id, name (nombre), email (se usa como username), password, role, status |
| `calificaciones` | id, fecha, estudiante, docente, asignatura, carrera, universidad, periodo, actividad_evaluada, porcentaje, nota |

El script está en `src/main/resources/schema.sql` (ejecútelo completo en TiDB).

## Endpoints

Todos requieren el header `Authorization: Bearer <accessToken>` (se obtiene con el login).

| Método | URL | Body |
|--------|-----|------|
| POST | `/api/auth/login` (sin token) | `{"email":"admin@example.com","password":"Admin1234!"}` |
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

## Consumo desde Kodular (componente Web)

1. Login: `Web.Url = <URL-render>/api/auth/login`,
   `Web.RequestHeaders = [["Content-Type","application/json"]]`,
   `Web.PostText` con el JSON de email/password.
   En `Web.GotText` → `Web.JsonTextDecodeWithDictionaries(responseContent)` → guardar `accessToken` en una variable global (o TinyDB).
2. Resto de peticiones: `Web.RequestHeaders = [["Authorization","Bearer " + token], ["Content-Type","application/json"]]`.
   - Listar: `Web.Get`
   - Crear: `Web.PostText(json)`
   - Editar: `Web.PutText(json)` con Url `.../api/calificaciones/{id}`
   - Eliminar: `Web.Delete` con Url `.../api/calificaciones/{id}`
3. En `Web.GotText` revisar `responseCode` (200/201/204 = éxito; 401 = volver a hacer login).
4. Render plan Free se duerme tras 15 min: la primera petición puede tardar ~50 s; suba `Web.Timeout`.
