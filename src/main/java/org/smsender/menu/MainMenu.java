package org.smsender.menu;

import org.smsender.script.ScriptExecutor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MainMenu {
    private static final Settings settings = new Settings();
    private static  boolean smsEnabled = false;

    public static void start(){
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
        boolean alive = true;
        while (alive){
            System.out.println("""
                This software is for notification sending using provided phone number list; \n
                Read documentation on this page https://github.com/MainKernel/SMSender \n
                """);
            printMenu();
            String choice = sc.nextLine();

            switch (choice){
                case "1":
                    printSettingsMenu();
                    String in = sc.nextLine();
                    switch (in){
                        case "1":
                            if (smsEnabled) {
                                smsEnabled = false;
                                settings.removeMessengers("SMS");
                            } else {
                                settings.setMessengers("SMS");
                            }
                            break;
                        case "2":
                            System.out.println("Enter text for SMS message: \n");
                            settings.setSmsText(sc.nextLine());
                            System.out.println("You SMS message text: " + settings.getSmsText());
                            break;
                        case "3":
                            System.out.println("Enter text for Messenger message: \n");
                            settings.setMessengerText(sc.nextLine());
                            System.out.println("You Messenger message text: " + settings.getSmsText());
                            break;
                        case "4":
                            continue;
                    }
                    break;
                case "2":
                    if(smsEnabled){
                        if(settings.getSmsText()!=null && settings.getMessengerText()!=null){
                            try {
                                ScriptExecutor.executeScript(settings);
                            } catch (IOException e) {
                                System.out.println("Something goes wrong (((");
                            }
                        }else {
                            System.out.println("Set text for SMS and Messengers on Settings.\n");
                        }
                    } else if (settings.getMessengers() != null) {
                        try {
                            ScriptExecutor.executeScript(settings);
                        } catch (IOException e) {
                            System.out.println("Something goes wrong (((");
                        }
                    }else {
                        System.out.println("Set text for SMS and Messengers on Settings.\n");
                    }

                    break;
                case "3":
                    alive = false;
                    break;
                default:
                    break;
            }
        }
    }

    private static void printMenu(){
        System.out.flush();
        System.out.println(""" 
                \r
                1. Settings \n
                2. Start sending \n
                3. Exit \n
                Enter your choice:
                """);
    }

    private static void printSettingsMenu(){
        System.out.flush();
        System.out.println("""
                \r
                1. Enable/Disable SMS (disabled by default) \n
                2. Set SMS text \n
                3. Set Messenger text \n
                4. Back \n
                Enter your choice:
                """);
    }
}
