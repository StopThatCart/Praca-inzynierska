package com.example.yukka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

@Component
public class PythonPlantSeeder {
    private static final Logger LOGGER = Logger.getLogger(PythonPlantSeeder.class.getName());

    
    /** 
     * @return String
     */
    public String runPythonScript() {
        //System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        String scriptPath = Paths.get("Scrapper", "import_to_neo4j.py").toAbsolutePath().toString();
        String scriptDir = Paths.get("Scrapper").toAbsolutePath().toString();
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "-u", scriptPath);
        processBuilder.directory(new java.io.File(scriptDir));
        processBuilder.redirectErrorStream(true);
        StringBuilder output = new StringBuilder();

        try {
            System.out.println("\n\nURUCHAMIANIE SKRYPTU PYTHON:\n\n");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;

            while ((line = reader.readLine()) != null) {
                LOGGER.log(Level.INFO, line);
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return output.toString();
            } else {
                return "Error running script, exit code: " + exitCode;
            }

        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred: ", e);
            return "Exception occurred: " + e.getMessage();
        }
    }
}
