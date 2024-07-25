package utils;

import models.CompteClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    public static Map<String, Integer> calculateSexeStatistics(List<CompteClient> compteClients) {
        Map<String, Integer> sexeStatistics = new HashMap<>();

        int maleCount = 0;
        int femaleCount = 0;
        int otherCount = 0;

        for (CompteClient compteClient : compteClients) {
            String sexe = compteClient.getSexe().toLowerCase(); // Assuming sexe is a String attribute
            switch (sexe) {
                case "male":
                    maleCount++;
                    break;
                case "female":
                    femaleCount++;
                    break;
                default:
                    otherCount++;
                    break;
            }
        }

        sexeStatistics.put("Male", maleCount);
        sexeStatistics.put("Female", femaleCount);
        sexeStatistics.put("Others", otherCount);

        return sexeStatistics;
    }



}

