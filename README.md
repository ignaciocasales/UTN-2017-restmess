# Proyecto Segundo Parcial - REST Message Service

##### Desarrollado por

- Ignacio Casales

## Características

| HTTP Method |             URL             |                    Respuesta                    |   HTTP Status   | Desarrollo |
|:-----------:|:---------------------------:|:-----------------------------------------------:|:---------------:|:----------:|
|     GET     | /user/list                  | Devuelve en JSON todos los usarios del sistema. |   200/204 /500  |     ...    |
|     POST    | /user/create                | Crea un usuario en el sistema.                  |     201/500     |     ...    |
|    DELETE   | /user/delete/{username}     | Borra un usuario por su username.               |     200/500     |     ...    |
|     POST    | /message/send               | Envia un mensaje.                               |     200/500     |     ...    |
|    PATCH    | /message/{id}/delete        | Marca un mensaje como borrado.                  |     200/500     |     ...    |
|     GET     | /message/{username}/inbox   | Devuelve en JSON el inbox de un usuario.        | 200/403/404/500 |     ...    |
|     GET     | /message/{username}/starred | Devuelve en JSON los favoritos de un usuario.   | 200/403/404/500 |     ...    |
|     GET     | /message/{username}/trash   | Devuelve en JSON el trash de un usuario.        | 200/403/404/500 |     ...    |
|     POST    | /api/login                  | Loguea un usuario.                              |   200/401/500   |     ...    |
|     GET     | /api/logout                 | Desloguea un usuario.                           |     200/500     |     ...    |

---

_Universidad Tecnológica Nacional - 2017_