package org.smsender.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberParser {
    public  static List<String> parsePhone(String filePath) throws IOException {
        List<String> phones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null){

                phones.add(phoneValidation(line).trim());
            }
        }
        return  phones;
    }

    private  static String phoneValidation(String phoneLine){
        if(phoneLine.startsWith("+380")){
            return phoneLine;
        } else {
            return "+38" + phoneLine;
        }
    }

}
