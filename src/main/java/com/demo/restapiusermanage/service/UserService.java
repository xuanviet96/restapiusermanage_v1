package com.demo.restapiusermanage.service;

import com.demo.restapiusermanage.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {

    Map<String, Object> retrieveUser(int pageNum, String sortField, String sortDirection, String term);
//    Map<String, Object> retrieveUser(UserPaginationDto userPaginationDto);

    User findOne(Long id);

    void delete(Long id);

    User save(User user);

    User updateUser(User user, Long id);

    void deleteUser(Long id);

    Page<User> search(String term, Pageable pageable);
}
