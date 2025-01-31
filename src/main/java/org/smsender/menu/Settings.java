package org.smsender.menu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    private List<String> messengers = new ArrayList<>();

    public List<String> getMessengers() {
        return messengers;
    }

    public void setMessengers(List<String> messengers) {
        this.messengers = messengers;
    }

    public String getExcelFilename() {
        return excelFilename;
    }

    public void setExcelFilename(String excelFilename) {
        this.excelFilename = excelFilename;
    }

    public Path getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(Path excelPath) {
        this.excelPath = excelPath;
    }

    public String getPhoneFile() {
        return phoneFile;
    }

    public void setPhoneFile(String phoneFile) {
        this.phoneFile = phoneFile;
    }

    public Path getPhoneFilePath() {
        return phoneFilePath;
    }

    public void setPhoneFilePath(Path phoneFilePath) {
        this.phoneFilePath = phoneFilePath;
    }

    private String excelFilename;
    private Path excelPath;
    private String phoneFile;
    private Path phoneFilePath;

    Settings(){
        messengers = List.of("WhatsApp", "Viber");
        excelFilename = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        phoneFile = "phones.txt";
        try {
            String jarPath = URLDecoder.decode(
                    Settings.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath(),
                    "UTF-8"
            );
            Path path = Paths.get(jarPath);
            excelPath = path.getParent();
            phoneFilePath = path.getParent();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    Settings(List<String> messengers, String excelFilename,
             Path excelPath, String phoneFile, Path phoneFilePath) {
        this.messengers = messengers;
        this.excelFilename = excelFilename;
        this.excelPath = excelPath;
        this.phoneFile = phoneFile;
        this.phoneFilePath = phoneFilePath;
    }

}
