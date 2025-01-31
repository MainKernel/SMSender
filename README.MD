## SMSender 
#### (Linux only)
___
This application linux only, coz bash script is used to interact with smartphone

## Setup
 #### 1. Connecting Phone Using ADB
For this step you need an android phone with installed Viber and WhatsApp

 ##### Enable Developer Options & USB Debugging

    1. Enable Developer Options:

     *   Go to Settings > About Phone.

     *   Tap Build Number 7 times until you see "You are now a developer."

    2. Turn on USB Debugging:

     *   Go to Settings > Developer Options (or System > Advanced > Developer Options).

     *   Enable USB Debugging.

##### Install ADB on Your Computer

In my case I am using Arch with pacman package manager for me the installation command is:

    `sudo pacman -Sy android-tools`

This package contains adb

##### Connect Your Phone via USB

    Use a USB cable to connect your phone to the computer.

    On your phone, when prompted, allow USB debugging (check "Always allow" if desired).

##### Verify the Connection

   1. Open a terminal/command prompt.
   2. Run
    `adb devices`

* If your device appears (e.g., ABCDEF123456 device), it’s connected.
* If it shows unauthorized, check your phone to accept the debugging prompt.

##### Packing project into fatJar

To package project with all dependencies, copy this project cd to this folder and run

`./gradelew fatJar`

This compiles project into `SMSender.jar`

Then copy next files into one folder:
`   SMSender.jar
    phones.txt
    sendScript.sh`

Open terminal and run:
`java -jar SMSender.jar`

### Program Interface

When you start program navigate to Settings:
* Enter text message for SMS
* Enter text message for Messengers
* Enable SMS sending if needed, it`s disable by default

### Before start sending
Copy phone numbers to `phones.txt`, one number for one line.
Then unlock you\`re connected to the computer phone, and press `2. Start sending`.
Now you phone start sending messages to all contacts you add. After finish, it`s generate an Excel document 
with phone number and messenger that used to deliver message to contact.  