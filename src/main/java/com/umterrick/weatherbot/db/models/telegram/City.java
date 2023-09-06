package com.umterrick.weatherbot.db.models.telegram;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Entity
@Table(name = "cities")
@Getter
@Data
@RequiredArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private String country;
    @Column
    private String name;

    @ManyToMany(mappedBy = "cities", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<TelegramUser> users;

}
