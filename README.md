# Proyecto Segundo Parcial - REST Message Service

[![Travis branch](https://img.shields.io/travis/ignaciocasales/UTN-2017-restmess/master.svg)](https://travis-ci.org/ignaciocasales/UTN-2017-restmess) [![Codecov](https://img.shields.io/codecov/c/github/codecov/UTN-2017-restmess/master.svg)](https://codecov.io/gh/ignaciocasales/UTN-2017-restmess)

##### Desarrollado por

- Ignacio Casales

## Características

| HTTP Method |             Endpoint             |                            Respuesta                           |   HTTP Status   |
|:-----------:|:--------------------------------:|:--------------------------------------------------------------:|:---------------:|
|     GET     | /api/users                       | Devuelve en JSON todos los usarios del sistema.                | 200/204/403/500 |
|     GET     | /api/users/{name}                | Devuelve en JSON todos los usarios con ese nombre del sistema. | 200/204/403/500 |
|     GET     | /api/users/{username}            | Devuelve en JSON un usuario.                                   | 200/204/403/500 |
|     POST    | /api/users                       | Crea un usuario en el sistema.                                 |     201/500     |
|    DELETE   | /api/users/{username}            | Borra un usuario por su username.                              |   200/403/500   |
|     GET     | /api/messages/{username}         | Devuelve en JSON el inbox de un usuario.                       | 200/204/403/500 |
|     GET     | /api/messages/{username}/sent    |                                                                | 200/204/403/500 |
|     GET     | /api/messages/{username}/starred | Devuelve en JSON los favoritos de un usuario.                  | 200/204/403/500 |
|     GET     | /api/messages/{username}/trash   | Devuelve en JSON el trash de un usuario.                       | 200/204/403/500 |
|     POST    | /api/messages                    | Crea y envia un mensaje.                                       |     201/500     |
|    PATCH    | /api/messages/{id}               | Modifica un mensaje.                                           |     200/500     |
|     GET     | /                                | Index.                                                         |   200/204/500   |
|     POST    | /login                           | Loguea un usuario.                                             |   200/401/500   |
|     GET     | /logout                          | Desloguea un usuario.                                          |     200/500     |

---

_Universidad Tecnológica Nacional - 2017_