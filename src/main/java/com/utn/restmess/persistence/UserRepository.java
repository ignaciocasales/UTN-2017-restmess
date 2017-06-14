package com.utn.restmess.persistence;

import com.utn.restmess.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * This is the repository interface, this will be automatically implemented by Spring
 * in a bean with the same name with changing case The bean name will be userRepository.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByFirstName(String firstname);

    int countByUsername(String username);

    int countByEmail(String email);
}
