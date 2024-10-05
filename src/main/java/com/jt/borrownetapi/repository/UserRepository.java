package com.jt.borrownetapi.repository;

import com.jt.borrownetapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmailIgnoreCase(String email);
}
