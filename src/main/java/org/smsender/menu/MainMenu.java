package org.smsender.menu;

import org.smsender.script.ScriptExecutor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MainMenu {
    private static final Settings settings = new Settings();
    private static  boolean smsEnabled = false;
    private static boolean waEnabled = false;
    private static boolean viberEnabled = false;

    public static void start(){
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
        boolean alive = true;
        while (alive){
            System.out.println("""
                \n
                ---------------------------------------------------------------------------
                This software is for notification sending using provided phone number list; \n
                Read documentation on this page https://github.com/MainKernel/SMSender \n
                ---------------------------------------------------------------------------
                \n
                """);
            printMenu();
            String choice = sc.nextLine();

            switch (choice){
                case "1":
                    boolean setMenu = true;
                    while (setMenu){
                    printSettingsMenu();
                    String in = sc.nextLine();
                    switch (in){
                        case "1":
                            if (smsEnabled) {
                                smsEnabled = false;
                                settings.removeMessengers("SMS");
                            } else {
                                smsEnabled = true;
                                settings.setMessengers("SMS");
                            }
                            System.out.println(smsEnabled?"Enabled":"Disabled");
                            break;
                        case "2":
                            if(waEnabled){
                                waEnabled = false;
                                settings.removeMessengers("WhatsApp");
                            } else {
                                waEnabled = true;
                                settings.setMessengers("WhatsApp");
                            }
                            break;
                        case "3":
                            if(viberEnabled){
                                viberEnabled = false;
                                settings.removeMessengers("Viber");
                            } else {
                                viberEnabled = true;
                                settings.setMessengers("Viber");
                            }
                            break;
                        case "4":
                            System.out.println("Enter text for SMS message: \n");
                            settings.setSmsText(sc.nextLine());
                            System.out.println("You SMS message text: " + settings.getSmsText());
                            break;
                        case "5":
                            System.out.println("Enter text for Messenger message: \n");
                            settings.setMessengerText(sc.nextLine());
                            System.out.println("You Messenger message text: " + settings.getSmsText());
                            break;
                        case "6":
                            setMenu = false;
                    }
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
        System.out.println("-------------------------------");
        System.out.println("SMS: ");
        System.out.println(smsEnabled?"Enabled":"Disabled");
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("WhatsApp: ");
        System.out.println(waEnabled?"Enabled":"Disabled");
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("Viber: ");
        System.out.println(viberEnabled?"Enabled":"Disabled");
        System.out.println("-------------------------------");
        System.out.println("""
                1. Enable/Disable SMS \n
                2. Enable/Disable WhatsApp
                3. Enable/Disable Viber
                4. Set SMS text \n
                5. Set Messenger text \n
                6. Back \n
                Enter your choice:
                """);
    }
}
