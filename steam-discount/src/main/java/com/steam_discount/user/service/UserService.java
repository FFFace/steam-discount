package com.steam_discount.user.service;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.common.security.jwt.JwtUtil;
import com.steam_discount.common.smtp.MailService;
import com.steam_discount.user.entity.Login;
import com.steam_discount.user.entity.PasswordDTO;
import com.steam_discount.user.entity.RefreshToken;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserDTO;
import com.steam_discount.user.entity.UserRole;
import com.steam_discount.user.entity.responseDTO.UserInfoResponseDTO;
import com.steam_discount.user.repository.RefreshTokenRepository;
import com.steam_discount.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    private final MailService mailService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private String passwordEncode(String password){
        return passwordEncoder.encode(password);
    }

    private boolean passwordMatch(User user, String password){
        return passwordEncoder.matches(password, user.getPassword());
    }

    public List<UserInfoResponseDTO> findAllUsers(){
        List<User> userList = userRepository.findAll();
        List<UserInfoResponseDTO> userInfoResponseDTOList = new ArrayList<>();
        userList.forEach(user -> {
            userInfoResponseDTOList.add(user.toInfoResponseDTO());
        });
        return userInfoResponseDTOList;
    }

    private User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public void saveNewUser(UserDTO userDTO){
        String code = getVerifyCodeNSendCodeToEmail(userDTO);
        User user = userDTO.toEntity();
        user.setVerifyCode(code);
        user.setPassword(passwordEncode(user.getPassword()));
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
        user.setRole(UserRole.USER);
        save(user);
        return true;
    }

    public boolean duplicateUserEmail(String email){
        return userRepository.findByEmail(email).isEmpty();
    }

    public boolean duplicateUserNickname(String nickname){
        return userRepository.findByNickname(nickname).isEmpty();
    }

    public void updateNickname(String nickname, User user){
        if(!duplicateUserNickname(nickname)){
            throw new CustomException(ErrorCode.ALREADY_USED_NICKNAME);
        }

        user.setNickname(nickname);
        userRepository.save(user);
    }

    public void updatePassword(PasswordDTO passwordDTO, User user){
        if(!passwordMatch(user, passwordDTO.getOldPassword())){
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        } else if(!passwordDTO.getNewPassword().equals(passwordDTO.getNewPasswordValid())){
            throw new CustomException(ErrorCode.NOT_MATCH_NEW_PASSWORD);
        }

        String newPassword = passwordEncode(passwordDTO.getNewPassword());
        user.setPassword(newPassword);

        userRepository.save(user);
    }

    @Transactional
    public void disableUser(String nickname, User admin){
        if(admin.getRole() != UserRole.ADMIN){
            throw new CustomException(ErrorCode.NO_HAVE_AUTHORITY);
        }

        User user = userRepository.findByNickname(nickname).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_USER));

        disableUser(user);
    }

    @Transactional
    public void disableUser(User user){
        user.disable();
        userRepository.save(user);

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByEmail(user.getEmail());
        if(optionalRefreshToken.isPresent()){
            RefreshToken token = optionalRefreshToken.get();
            refreshTokenRepository.delete(token);
        }
    }

    public void enableUser(String nickname, User admin){
        if(admin.getRole() != UserRole.ADMIN){
            throw new CustomException(ErrorCode.NO_HAVE_AUTHORITY);
        }

        User user = userRepository.findByNickname(nickname).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_USER));

        user.enable();
        userRepository.save(user);
    }

    public void login(Login login, HttpServletResponse response){
        User user = userRepository.findByEmail(login.getEmail()).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!passwordMatch(user, login.getPassword()))
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);

        if(user.getVerify() == 'F'){
            throw new CustomException(ErrorCode.NOT_VERIFY_EMAIL);
        }

        createNewToken(login.getEmail(), response);
    }

    public void logout(User user, HttpServletResponse response){
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(user.getEmail()).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));

        Cookie cookie = new Cookie(jwtUtil.getREFRESH_TOKEN_COOKIE_NAME(), null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(true);

        response.addCookie(cookie);

        refreshTokenRepository.delete(refreshToken);
    }




    // NOTE: 여기서부터 Token 관련 메소드

    private void createNewToken(String email, HttpServletResponse response){
        String accessToken = jwtUtil.generateAccessToken(email);

        response.setHeader(jwtUtil.getACCESS_TOKEN_HEADER_NAME(), accessToken);

        String refreshToken = jwtUtil.generateRefreshToken(email);
        Cookie cookie = new Cookie(jwtUtil.getREFRESH_TOKEN_COOKIE_NAME(), refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtUtil.getRefreshCookieMaxAge());
        cookie.setSecure(true);

        response.addCookie(cookie);

        saveRefreshToken(email, refreshToken);
    }

    private void saveRefreshToken(String email, String token){
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(new RefreshToken());
        refreshToken.setEmail(email);
        refreshToken.setToken(token);

        refreshTokenRepository.save(refreshToken);
    }
}
