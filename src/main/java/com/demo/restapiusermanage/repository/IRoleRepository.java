package com.demo.restapiusermanage.repository;

import com.demo.restapiusermanage.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Integer> {

}
