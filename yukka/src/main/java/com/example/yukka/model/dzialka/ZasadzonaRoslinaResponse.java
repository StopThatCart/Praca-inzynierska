package com.example.yukka.model.dzialka;

import com.example.yukka.model.roslina.RoslinaResponse;

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

    private RoslinaResponse roslina;
    private int x;
    private int y;

    private int[] tabX;
    private int[] tabY;

    private byte[] obraz;


 
}
