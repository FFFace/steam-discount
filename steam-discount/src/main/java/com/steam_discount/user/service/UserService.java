package com.steam_discount.user.service;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserDTO;
import com.steam_discount.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;



    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public User saveNewUser(UserDTO userDTO){
        return userRepository.save(userDTO.toEntity());
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
