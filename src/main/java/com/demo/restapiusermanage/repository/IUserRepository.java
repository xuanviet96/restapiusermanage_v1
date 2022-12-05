package com.demo.restapiusermanage.repository;

import com.demo.restapiusermanage.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1% OR convert(u.age , char(50)) LIKE %?1%")
    Page<User> search(String term, Pageable pageable);

    Optional<User> findByEmail(String email);
}
