package com.apica.UserService.entity;

import com.apica.UserService.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity(name = "auth")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Auth {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "_id", columnDefinition = "uuid")
    UUID id;

    String email;

    String password;

    String phoneNo;

    @CreationTimestamp
    Timestamp createdDate;

    @UpdateTimestamp
    Timestamp updatedDate;

    @Enumerated(EnumType.STRING)
    Role role;
}
