/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.yukka.model.social.post;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private String postId;
    private String tytul;
    private String opis;
    private Integer ocenyLubi;
    private Integer ocenyNieLubi;
    private Integer liczbaKomentarzy;
    private String uzytkownik;
    private byte[] obraz;
    private LocalDateTime dataUtworzenia;
}

