package com.example.yukka.model.dzialka;

public class DzialkaManager {
    private static final int SIZE = 20;
    private Dzialka[][] pola;

    public DzialkaManager() {
        pola = new Dzialka[SIZE][SIZE];
        initializeDzialki();
    }

    private void initializeDzialki() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                pola[x][y] = Dzialka.builder()
                                       .numer(x * SIZE + y)
                                       .pola(new int[SIZE][SIZE]) 
                                       .build();
            }
        }
    }

    public Dzialka getDzialka(int x, int y) {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            return pola[x][y];
        } else {
            throw new IndexOutOfBoundsException("Invalid coordinates");
        }
    }

    public void setDzialka(int x, int y, Dzialka dzialka) {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            pola[x][y] = dzialka;
        } else {
            throw new IndexOutOfBoundsException("Invalid coordinates");
        }
    }
}
