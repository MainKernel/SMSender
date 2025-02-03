#!/bin/bash

# Validate first argument (messenger type), supported: Viber/WhatsApp/SMS
# Second argument is the phone number, third is the message, fourth controls display

function stop_app {
    local package="$1"
    adb shell am force-stop "$package" > /dev/null
    sleep 2
#    adb shell pkill -f "$package" > /dev/null
}

function display_setting {
    if [[ $1 == "start" ]]; then
        adb shell settings put global stay_on_while_plugged_in 3 > /dev/null
    else
        adb shell settings delete global stay_on_while_plugged_in > /dev/null
    fi
}

function send_message {
  for word in $1
  do
        adb shell input text "$word"
        sleep 0.2
        adb shell input keyevent 62  # Right arrow to move focus
  done
        sleep 1
        adb shell input tap 1000 1500  # Tap send button
}

# Main execution
messenger_type="$1"
phone_number="$2"
message="$3"
display_mode="$4"

display_setting "$display_mode"

case $messenger_type in
    "WhatsApp")
        adb logcat -c
        adb shell am start -a android.intent.action.VIEW -d "https://wa.me/${phone_number}" > /dev/null
        sleep 4
        whatsappError=$(adb logcat -d | grep -i "Displayed com.whatsapp/.Conversation")
        if [[ -n $whatsappError ]] ; then
            adb shell input tap 300 2200  # Tap message field
            send_message "$message"
            sleep 2
            stop_app com.whatsapp
            echo "WhatsApp"
        else
            stop_app com.whatsapp
            echo "WAerrorEvent"
        fi
        ;;

    "Viber")
        adb logcat -c
        adb shell "am start -a android.intent.action.VIEW -d 'viber://chat?number=${phone_number}' com.viber.voip" > /dev/null
        sleep 5
        viberError=$(adb shell logcat -d | grep -E "act=com.viber.voip.action.SYSTEM_DIALOG|(startActivityAsUser) (BAL_ALLOW_VISIBLE_WINDOW) result code=0")
        if [[ -n $viberError ]] ; then
            sleep 3
            adb shell input tap 300 2200  # Tap message field
            send_message "$message"
            sleep 2
            stop_app com.viber.voip
            echo "Viber"
        else
            stop_app com.viber.voip
            echo "VIerrorEvent"
        fi
        ;;

    "SMS")
        adb shell am start -a android.intent.action.SENDTO -d "sms:${phone_number}" > /dev/null
        sleep 2
        adb shell input tap 400 2200  # Tap message field
        sleep 1
        send_message "$message"
        sleep 2
        adb shell am force-stop com.google.android.apps.messaging
        echo "SMS"
        ;;

    *)
        echo "Invalid messenger type"
        exit 1
        ;;
esac