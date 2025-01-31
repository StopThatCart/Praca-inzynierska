package com.example.yukka.seeder.rosliny;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa HeightProcessor przetwarza dane dotyczące wysokości i oblicza ogólną wysokość.
 * 
 * <ul>
 * <li><strong>getWysokosc</strong> - Metoda pobiera wysokości w formie Stringa, przetwarza je i zwraca obiekt Wysokosc reprezentujący ogólną wysokość.</li>
 * <li><strong>calculateOverallHeight</strong> - Metoda oblicza ogólną minimalną i maksymalną wysokość z listy obiektów Wysokosc.</li>
 * <li><strong>processHeights</strong> - Metoda przetwarza String zawierający wysokości i zwraca listę obiektów Wysokosc.</li>
 * <li><strong>processHeightString</strong> - Metoda przetwarza pojedynczy String reprezentujący wysokość i zwraca tablicę Stringów zawierającą minimalną i maksymalną wysokość.</li>
 * <li><strong>heightQueryStringToClass</strong> - Metoda konwertuje Stringi reprezentujące minimalną i maksymalną wysokość na obiekt Wysokosc.</li>
 * </ul>
 */
public class HeightProcessor {

    
    /** 
     * @param heights
     * @return Wysokosc
     */
    public static Wysokosc getWysokosc(String heights) {
        return calculateOverallHeight(processHeights(heights));
    }
    public static Wysokosc calculateOverallHeight(List<Wysokosc> heights) {
        if (heights == null || heights.isEmpty()) {
            return new Wysokosc(0, 0);
        }

        double overallMin = Double.MAX_VALUE;
        double overallMax = Double.MIN_VALUE;

        for (Wysokosc wysokosc : heights) {
            if (wysokosc.getMin() < overallMin) {
                overallMin = wysokosc.getMin();
            }
            if (wysokosc.getMax() > overallMax) {
                overallMax = wysokosc.getMax();
            }
        }

        return new Wysokosc(overallMin, overallMax);
    }

    public static List<Wysokosc> processHeights(String heights) {
        List<Wysokosc> result = new ArrayList<>();
        if (heights == null || heights.isEmpty()) return result;

        String[] heightArray = heights.split(",");
        for (String height : heightArray) {
            String cleanHeight = height.trim();
            if (cleanHeight.isEmpty()) continue;

            String[] minMax = processHeightString(cleanHeight);
            
            Wysokosc wysokosc = heightQueryStringToClass(minMax[0], minMax[1]);
            result.add(wysokosc);
        }

        return result;
    }


    private static String[] processHeightString(String height) {
        String[] minMax = new String[2];
        if (height.contains("powyżej")) {
            minMax[0] = height.split("powyżej")[1].trim();
        } else if (height.contains("od") && height.contains("do")) {
            String[] parts = height.split("od")[1].split("do");
            minMax[0] = parts[0].trim().replace("m", "");
            minMax[1] = parts[1].trim().replace("m", "");
        } else if (height.contains("do")) {
            minMax[1] = height.split("do")[1].trim().replace("m", "");
        } else if (height.contains("od")) {
            minMax[0] = height.split("od")[1].trim().replace("m", "");
        } else {
            minMax[0] = minMax[1] = null;
        }
        return minMax;
    }

    private static Wysokosc heightQueryStringToClass(String minHeight, String maxHeight) {
        double min = minHeight != null ? Double.parseDouble(minHeight.replace(',', '.')) : 0;
        double max = maxHeight != null ? Double.parseDouble(maxHeight.replace(',', '.')) : min;

        return new Wysokosc(min, max);
    }
}
