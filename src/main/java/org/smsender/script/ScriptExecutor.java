package org.smsender.script;

import org.smsender.entity.PhoneObject;
import org.smsender.menu.Settings;
import org.smsender.parser.PhoneNumberParser;
import org.smsender.saver.ExcelSaver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScriptExecutor {

    private static final String scriptFileName = "sendScript.sh";
    private static final List<String> SCRIPT_ERROR_EVENTS = List.of("WAerrorEvent", "VIerrorEvent");

    public static void executeScript(Settings settings){
        List<String> strings;
        List<PhoneObject> phoneObjects = new ArrayList<>();
        String script = settings.getScriptPath().toString() + "/" +  scriptFileName;
        String displayArg = "off";

        try {
            strings = PhoneNumberParser.parsePhone((settings.getPhoneFilePath() + "/" + settings.getPhoneFile()));

            for (int i = 0; i < strings.size(); i++) {
                PhoneObject phoneObject;
                if(i == strings.size() - 1){
                    phoneObject = sendMessage(strings.get(i), script, displayArg, settings);
                } else {
                    phoneObject = sendMessage(strings.get(i), script, "start", settings);
                }
                phoneObjects.add(phoneObject);
            }

            ExcelSaver.saveNumber(phoneObjects,settings.getExcelPath(),settings.getExcelFilename());
            System.out.println("All phone numbers processed and saved into " + settings.getExcelFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PhoneObject sendMessage(String phoneNumber, String script, String displayArg, Settings settings) throws IOException {
        String status;
        List<String> messengers = settings.getMessengers();
        for (String messenger : messengers ) {
            ProcessBuilder pb = new ProcessBuilder(script, messenger,
                    phoneNumber, getText(messenger, settings), displayArg);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            status = output.toString().trim();

            if (!SCRIPT_ERROR_EVENTS.contains(status)) {
                return new PhoneObject(phoneNumber, status);
            }
        }

        return new PhoneObject(phoneNumber,"Not found contact");
    }

    private static String getText(String messenger, Settings settings){
        if (messenger.equals("SMS")) {
            return settings.getSmsText();
        }
        return settings.getMessengerText();
    }
}
