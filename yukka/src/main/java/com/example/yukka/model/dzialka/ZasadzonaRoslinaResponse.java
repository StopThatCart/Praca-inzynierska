package com.example.yukka.model.dzialka;

import com.example.yukka.model.roslina.Roslina;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZasadzonaRoslinaResponse {

    private Roslina roslina;
    private int x;
    private int y;
    private byte[] obraz;


 
}
