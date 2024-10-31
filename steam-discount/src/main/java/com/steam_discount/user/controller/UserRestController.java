package com.steam_discount.user.controller;


import com.google.firebase.auth.UserInfo;
import com.steam_discount.common.security.jwt.user.CustomUser;
import com.steam_discount.user.entity.Login;
import com.steam_discount.user.entity.PasswordDTO;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserDTO;
import com.steam_discount.user.entity.VerifyEmail;
import com.steam_discount.user.entity.responseDTO.UserInfoResponseDTO;
import com.steam_discount.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    /**
     * 전체 사용자 목록을 검색합니다.
     * @return ResponseEntity<List<UserInfoResponseDTO>>
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserInfoResponseDTO>> getUserList(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    /**
     * 마이페이지에서 사용할 email 정보를 검색합니다.
     * @param customUser 로그인 된 사용자
     * @return ResponseEntity<String>
     */
    @GetMapping("/email")
    public ResponseEntity<String> getUserEmail(@AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(customUser.getUser().getEmail());
    }

    /**
     * 새로운 사용자를 등록합니다.
     * @param userDTO 새로 가입할 사용자 정보
     */
    @PostMapping("/create")
    public void createUser(@RequestBody @Valid UserDTO userDTO){
        userService.saveNewUser(userDTO);
    }

    /**
     * 사용자의 이메일 인증을 진행합니다.
     * @param verify 인증 정보
     * @return ResponseEntity<Boolean>
     */
    @PatchMapping("/verify")
    public ResponseEntity<Boolean> verifyUserEmail(@RequestBody VerifyEmail verify){
        return ResponseEntity.ok(userService.verifiedCode(verify.getEmail(), verify.getCode()));
    }

    /**
     * 이메일 중복검사를 진행합니다.
     * @param email 중복 검사 할 이메일
     * @return ResponseEntity<Boolean>
     */
    @GetMapping("/duplicate-email")
    public ResponseEntity<Boolean> duplicateUserEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.duplicateUserEmail(email));
    }

    /**
     * 닉네임 중복검사를 진행합니다.
     * @param nickname 중복검사 할 닉네임
     * @return ResponseEntity<Boolean>
     */
    @GetMapping("/duplicate-nickname")
    public ResponseEntity<Boolean> duplicateUserNickname(@RequestParam String nickname){
        return ResponseEntity.ok(userService.duplicateUserNickname(nickname));
    }

    /**
     * 닉네임을 변경합니다.
     * @param map 변경할 닉네임
     * @param customUser 변경할 사용자
     */
    @PatchMapping("/nickname")
    public void updateUserNickname(@RequestBody Map<String, String> map, @AuthenticationPrincipal CustomUser customUser){
        userService.updateNickname(map.get("nickname"), customUser.getUser());
    }

    /**
     * 비밀번호를 변경합니다.
     * @param passwordDTO 비밀번호 정보
     * @param customUser 변경할 사용자
     */
    @PatchMapping("/password")
    public void updateUserPassword(@RequestBody @Valid PasswordDTO passwordDTO, @AuthenticationPrincipal CustomUser customUser){
        userService.updatePassword(passwordDTO, customUser.getUser());
    }

    @PutMapping("/disable/{nickname}")
    public void disableUserByAdmin(@PathVariable String nickname, @AuthenticationPrincipal CustomUser customUser){
        userService.disableUser(nickname, customUser.getUser());
    }

    @PutMapping("/disable")
    public void disableUser(@AuthenticationPrincipal CustomUser customUser){
        userService.disableUser(customUser.getUser());
    }

    @PutMapping("/enable/{nickname}")
    public void enableUserByAdmin(@PathVariable String nickname, @AuthenticationPrincipal CustomUser customUser){
        userService.enableUser(nickname, customUser.getUser());
    }
}
