package com.example.yukka.model.dzialka;

import java.util.List;

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
public class DzialkaResponse {
    private Long id;
    private int numer;
    private String wlascicielNazwa;
    private List<ZasadzonaRoslinaResponse> zasadzoneRosliny;
    private int liczbaRoslin;


    
}
