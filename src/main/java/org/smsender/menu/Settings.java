package org.smsender.menu;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Settings {

    private final String excelFilename;
    private final Path excelPath;
    private final String phoneFile;
    private final Path phoneFilePath;
    private final Path scriptPath;
    private String messengerText;
    private String smsText;

    private  List<String> messengers;

    public List<String> getMessengers() {
        return messengers;
    }

    public void setMessengers(String messenger) {
        this.messengers.add(messenger);
    }

    public void removeMessengers(String messenger) {
        this.messengers.remove(messenger);
    }

    public String getExcelFilename() {
        return excelFilename;
    }

    public Path getExcelPath() {
        return excelPath;
    }

    public String getPhoneFile() {
        return phoneFile;
    }

    public Path getPhoneFilePath() {
        return phoneFilePath;
    }

    public Path getScriptPath() {
        return this.scriptPath;
    }


    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getMessengerText() {
        return messengerText;
    }

    public void setMessengerText(String messengerText) {
        this.messengerText = messengerText;
    }


    Settings() {

        messengers = List.of("WhatsApp", "Viber");
        excelFilename = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        phoneFile = "phones.txt";
        String jarPath = URLDecoder.decode(Settings.class.getProtectionDomain().getCodeSource()
                .getLocation().getPath(), StandardCharsets.UTF_8);
        Path path = Paths.get(jarPath);
        excelPath = path.getParent();
        phoneFilePath = path.getParent();
        scriptPath = path.getParent();
    }


}
