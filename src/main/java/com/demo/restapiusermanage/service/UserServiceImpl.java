package com.demo.restapiusermanage.service;


import com.demo.restapiusermanage.entity.User;
import com.demo.restapiusermanage.exception.ResourceNotFoundException;
import com.demo.restapiusermanage.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private IUserRepository repo;

    @Override
    public Map<String, Object> retrieveUser(int pageNum, String sortField, String sortDirection, String term) {
        // service.retrieveUser(1, "firstName", "ASC", null);
        int perPage = 3;

        Map<String, Object> mapData = new LinkedHashMap<>();
        Sort.Order order = sortDirection.equals("ASC") ? Sort.Order.asc(sortField) : Sort.Order.desc(sortField);

        Pageable pageable = PageRequest.of(pageNum-1, perPage, Sort.by(order));

        Page<User> userPage = repo.search(term, pageable);

        mapData.put("total", userPage.getTotalElements());
        mapData.put("per_page", perPage);
        mapData.put("current_page", pageNum);
        mapData.put("last_page", userPage.getTotalPages());
        mapData.put("data", userPage.getContent());

        return mapData;
    }

//    @Override
//    public Map<String, Object> retrieveUser(UserPaginationDto userPaginationDto) {
//
//        Map<String, Object> mapData = new LinkedHashMap<>();
//        Sort.Order order = userPaginationDto.getSortDirection().equals("ASC") ? Sort.Order.asc(userPaginationDto.getSortField()) :
//                Sort.Order.desc(userPaginationDto.getSortField());
//
//        Pageable pageable = PageRequest.of(userPaginationDto.getPage()-1, userPaginationDto.getPerPage(), Sort.by(order));
//
//        Page<User> userPage = repo.search(userPaginationDto.getTerm(), pageable);
//
//        mapData.put("total", userPage.getTotalElements());
//        mapData.put("per_page", userPaginationDto.getPerPage());
//        mapData.put("current_page", userPaginationDto.getPage());
//        mapData.put("data", userPage.getContent());
//
//        return mapData;
//    }

    @Override
    public User findOne(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found !"));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public User save(User user) {
        int strength = 10; // work factor of bcrypt
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return repo.save(user);
    }

    @Override
    public User updateUser(User userDetails, Long id) {

        User existingUser = repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not Found!"));

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setAge(userDetails.getAge());
        existingUser.setEmail(userDetails.getEmail());

        repo.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUser(Long id) {

        //TODO: check if a user exist with given id?
        User existingUser = repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not Found!"));
        repo.deleteById(id);
    }

    @Override
    public Page<User> search(String term, Pageable pageable) {
        return null;
    }
}
