package com.utn.restmess.persistence;

import com.utn.restmess.entities.Message;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * This is the repository interface, this will be automatically implemented by Spring
 * in a bean with the same name with changing case The bean name will be messageRepository.
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
}
