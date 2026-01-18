package org.raflab.studsluzba.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Component
public class CoderLoader {
    
    private Set<String> validStudProgramOznake;
    
    public CoderLoader() {
        this.validStudProgramOznake = loadOznakePlana();
    }
    
    private Set<String> loadOznakePlana() {
        Set<String> oznake = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/coders/oznakaplana.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    oznake.add(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading oznakaplana.txt: " + e.getMessage());
        }
        return oznake;
    }
    
    public boolean isValidStudProgramOznaka(String oznaka) {
        return validStudProgramOznake.contains(oznaka);
    }
    
    public Set<String> getValidStudProgramOznake() {
        return new HashSet<>(validStudProgramOznake);
    }
}
