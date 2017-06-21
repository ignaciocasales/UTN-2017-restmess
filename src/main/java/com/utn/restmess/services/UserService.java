package com.utn.restmess.services;

import com.google.common.collect.Lists;
import com.utn.restmess.entities.Message;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ignacio on 6/10/17.
 * <p>
 * UserService
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private Encrypter encrypter;

    /**
     * Verifica que el usuario exista, y que su contraseña concuerde con
     * la proporcionada. Las contraseñas estan encriptadas.
     *
     * @param username String
     * @param password String
     * @return User
     */
    public User login(String username, String password) {

        User u = userRepository.findByUsername(username);

        if (u == null) {
            return null;
        }

        if (encrypter.matches(password, u.getPassword())) {
            return u;
        }

        return null;
    }

    /**
     * Graba un usuario en la base de datos. Devuelve el mismo usuario creado.
     *
     * @param userRequest UserRequest
     * @return User
     */
    private User newUser(UserRequest userRequest) {
        User u = new User();

        u.setFirstName(userRequest.getFirstName());
        u.setLastName(userRequest.getLastName());
        u.setAddress(userRequest.getAddress());
        u.setPhone(userRequest.getPhone());
        u.setCity(userRequest.getCity());
        u.setState(userRequest.getState());
        u.setCountry(userRequest.getCountry());
        u.setUsername(userRequest.getUsername());
        u.setEmail(userRequest.getPassword());
        u.setPassword(encrypter.encrypt(userRequest.getEmail()));

        return userRepository.save(u);
    }

    /**
     * Trae todos los usuarios.
     *
     * @return List<User>
     * @throws NoUsersException Si no hay usuarios registrados.
     */
    public List<User> getAll() throws NoUsersException {
        Iterable<User> userIterable = userRepository.findAll();

        List<User> userList = Lists.newArrayList(userIterable);

        if (userList.isEmpty()) {
            throw new NoUsersException("No hay usuarios.");
        }

        return userList;
    }

    /**
     * Obtiene un listado de usarios por nombre.
     *
     * @param name String
     * @return List<User>
     * @throws NoUsersException Si no encuntra ningun usuario.
     */
    public List<User> getByName(String name) throws NoUsersException {
        if (!name.isEmpty()) {
            String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1);

            List<User> userList = userRepository.findByFirstName(formattedName);

            if (userList.isEmpty()) {
                throw new NoUsersException("No hay usuarios con ese nombre.");
            }

            return userList;
        } else {
            throw new NoUsersException("Ingrese un usuario.");
        }
    }

    /**
     * Obtiene un usuario por su nombre de usuario unico.
     *
     * @param username String
     * @return User
     * @throws NoUsersException Si no encuntra el usuario proporcionado.
     */
    public User getByUsername(String username) throws NoUsersException {
        User u = userRepository.findByUsername(username);

        if (u == null) {
            throw new NoUsersException("No hay usuarios con ese username.");
        }

        return u;
    }

    /**
     * Crea un usuario.
     *
     * @param request UserRequest
     * @return User
     * @throws DuplicateUsernameException El nombre de usuario debe ser unico.
     * @throws DuplicateEmailException    El mail de contacto debe ser unico.
     */
    public User create(UserRequest request) throws DuplicateUsernameException, DuplicateEmailException {
        if (userRepository.countByUsername(request.getUsername()) > 0) {
            throw new DuplicateUsernameException("Nombre de usuario ya usado.");
        } else if (userRepository.countByEmail(request.getEmail()) > 0) {
            throw new DuplicateEmailException("Email ya registrado.");
        } else {
            return this.newUser(request);
        }
    }

    /**
     * Elimina un usuario y sus mensajes.
     *
     * @param username String
     */
    public void destroy(String username) {
        User u = userRepository.findByUsername(username);

        for (Message m :
                u.getMsgList()) {
            messageRepository.delete(m.getId());
        }

        userRepository.delete(u.getId());
    }

    User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
