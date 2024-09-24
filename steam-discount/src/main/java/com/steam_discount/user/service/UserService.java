package com.steam_discount.user.service;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.common.smtp.MailService;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserDTO;
import com.steam_discount.user.repository.UserRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final String AUTO_CODE_PREFIX = "AuthCode ";
    private final MailService mailService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private Long authCodeExpirationMillis;


    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public void saveNewUser(UserDTO userDTO){
        String code = getVerifyCodeNSendCodeToEmail(userDTO);
        User user = userDTO.toEntity();
        user.setVerifyCode(code);
        save(user);
    }

    private User save(User user){
        return userRepository.save(user);
    }

    private String getVerifyCodeNSendCodeToEmail(UserDTO userDTO) {
        String title = "회원가입을 위한 인증번호";
        String authCode = createCode();
        String text = "회원가입을 위해 아래의 코드를 사이트에서 입력해주세요."
            + "\n"
            + "\n"
            + authCode;

        mailService.sendEmail(userDTO.getEmail(), title, text);

        return authCode;
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_ALGORITHM);
        }
    }

    public boolean verifiedCode(String email, String authCode) {
        User user = userRepository.findByEmail(email).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!user.getVerifyCode().equals(authCode)){
            return false;
        }

        user.setVerify('T');
        user.setVerifyCode("");
        save(user);
        return true;
    }

    public boolean duplicateUserEmail(String email){
        return userRepository.findByEmail(email).isEmpty();
    }

    public boolean duplicateUserNickname(String nickname){
        return userRepository.findByNickname(nickname).isEmpty();
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
