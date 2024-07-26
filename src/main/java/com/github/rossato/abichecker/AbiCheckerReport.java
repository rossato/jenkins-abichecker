package com.github.rossato.abichecker;

import hudson.FilePath;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AbiCheckerReport {
    String verdict;
    int added;
    int removed;
    int type_problems_high;
    int type_problems_medium;
    int type_problems_low;
    int interface_problems_high;
    int interface_problems_medium;
    int interface_problems_low;
    int changed_constants;

    public AbiCheckerReport(FilePath htmlReport) throws IOException, InterruptedException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(htmlReport.read(), StandardCharsets.UTF_8));
        String line = reader.readLine();
        reader.close();
        if (line == null) return;

        String kvpairs[] = line.split("[ ;]");
        for (String kvpair : kvpairs) {
            String kv[] = kvpair.split(":");
            if (kv[0].equals("verdict")) {
                verdict = kv[1];
            } else if (kv[0].equals("added")) {
                added = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("removed")) {
                removed = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("type_problems_high")) {
                type_problems_high = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("type_problems_medium")) {
                type_problems_medium = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("type_problems_low")) {
                type_problems_low = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("interface_problems_high")) {
                interface_problems_high = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("interface_problems_medium")) {
                interface_problems_medium = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("interface_problems_low")) {
                interface_problems_low = Integer.parseInt(kv[1]);
            } else if (kv[0].equals("changed_constants")) {
                changed_constants = Integer.parseInt(kv[1]);
            }
        }
    }

    public boolean isCompatible() {
        return verdict.equals("compatible");
    }

    public int getAdditions() {
        return added;
    }

    public int getRemovals() {
        return removed;
    }

    public int getTypeProblemsHigh() {
        return type_problems_high;
    }

    public int getTypeProblemsMedium() {
        return type_problems_medium;
    }

    public int getTypeProblemsLow() {
        return type_problems_low;
    }

    public int getInterfaceProblemsHigh() {
        return interface_problems_high;
    }

    public int getInterfaceProblemsMedium() {
        return interface_problems_medium;
    }

    public int getInterfaceProblemsLow() {
        return interface_problems_low;
    }

    public int getChangedConstants() {
        return changed_constants;
    }

    public String getVerdict() {
        return verdict;
    }
}
