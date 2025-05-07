package com.ngntu10.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

import static com.ngntu10.util.Constants.PASSWORD_RESET_TOKEN_LENGTH;

@Entity
@Table(name = "password_reset_tokens", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"token"}, name = "uk_password_reset_tokens_token")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken extends AbstractBaseEntity {
    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_password_reset_tokens_user_id")
    )
    private User user;

    @Column(name = "token", nullable = false, length = PASSWORD_RESET_TOKEN_LENGTH)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
}
