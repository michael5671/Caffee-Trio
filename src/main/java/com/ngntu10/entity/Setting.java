package com.ngntu10.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"key"}, name = "uk_settings_key")
}, indexes = {
    @Index(columnList = "value", name = "idx_settings_value")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setting extends AbstractBaseEntity {
    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "value", columnDefinition = "text")
    private String value;
}
