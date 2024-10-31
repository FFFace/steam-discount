package com.steam_discount.user.entity;


import com.steam_discount.common.entity.BaseEntity;
import com.steam_discount.user.entity.responseDTO.UserInfoResponseDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String nickname;

    @Column
    private String product;

    @Column
    private String productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column
    private char verify;

    @Column
    private String verifyCode;

    public User(String email, String password, String nickname, String product, String productId, UserRole role){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.product = product;
        this.productId = productId;
        this.role = role;
        verify = 'F';
    }

    public UserInfoResponseDTO toInfoResponseDTO(){
        return new UserInfoResponseDTO(email, nickname, role.name(),
            getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), getDisable());
    }
}
