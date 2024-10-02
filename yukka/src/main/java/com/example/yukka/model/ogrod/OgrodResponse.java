package com.example.yukka.model.ogrod;

import java.util.List;

import com.example.yukka.model.dzialka.DzialkaResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OgrodResponse {
    private Long id;
    private String nazwa;
    private String wlascicielNazwa;

    private List<DzialkaResponse> dzialki;
    
}
