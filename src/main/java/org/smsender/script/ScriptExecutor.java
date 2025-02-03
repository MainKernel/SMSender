package org.smsender.script;

import org.smsender.entity.PhoneObject;
import org.smsender.menu.Settings;
import org.smsender.parser.PhoneNumberParser;
import org.smsender.saver.ExcelSaver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScriptExecutor {
    private static final String SCRIPT_NAME = "sendScript.sh";
    private static final List<String> SCRIPT_ERROR_EVENTS = List.of("WAerrorEvent", "VIerrorEvent");

    public static void executeScript(Settings settings) throws IOException {
        List<PhoneObject> results = new ArrayList<>();
        Path scriptPath = settings.getScriptPath().resolve(SCRIPT_NAME);
        List<String> phoneNumbers = PhoneNumberParser.parsePhone(
                settings.getPhoneFilePath().resolve(settings.getPhoneFile()).toString()
        );

        for (int i = 0; i < phoneNumbers.size(); i++) {
            String displayMode = (i == phoneNumbers.size() - 1) ? "off" : "start";
            PhoneObject result = processNumber(phoneNumbers.get(i), scriptPath, displayMode, settings);
            results.add(result);
        }

        ExcelSaver.saveNumber(results, settings.getExcelPath(), settings.getExcelFilename());
        System.out.println("Processed " + results.size() + " numbers. Saved to: " + settings.getExcelFilename());
    }

    private static PhoneObject processNumber(String number, Path scriptPath, String displayMode, Settings settings)
            throws IOException {
        for (String messenger : settings.getMessengers()) {
            Process process = new ProcessBuilder(
                    scriptPath.toString(),
                    messenger,
                    number,
                    getMessageContent(messenger, settings),
                    displayMode
            ).redirectErrorStream(true).start();

            String status = readProcessOutput(process);
            if (!SCRIPT_ERROR_EVENTS.contains(status)) {
                return new PhoneObject(number, status);
            }
        }
        return new PhoneObject(number, "No working messenger found");
    }

    private static String readProcessOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line.trim());
            }
        }
        return output.toString();
    }

    private static String getMessageContent(String messenger, Settings settings) {
        return "SMS".equals(messenger) ? settings.getSmsText() : settings.getMessengerText();
    }
}
