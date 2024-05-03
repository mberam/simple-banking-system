package com.leapwise.banking.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="\"customer\"")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="\"id\"")
    private Long id;

    @Column(name="\"name\"")
    private String name;

    @Column(name="\"address\"")
    private String address;

    @Column(name="\"email\"")
    private String email;

    @Column(name = "\"phone_number\"")
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

}
