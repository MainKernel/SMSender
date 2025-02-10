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

function send_message_whatsapp {
  adb shell am start -a android.intent.action.VIEW -d "https://wa.me/${1}/?text=${2}" > /dev/null
  sleep 2
  adb shell input tap 1000 2200
}

function send_viber_message {
  local phone_number="$1"
  local message="$2"


  local temp_file="/sdcard/temp_viber_message.txt"
  echo "$message" | adb shell "echo '$message' > $temp_file"
  sleep 2
  adb shell am start -a android.intent.action.VIEW -d "viber://forward?text=$(adb shell cat $temp_file | tr -d '\n')" > /dev/null
  sleep 2
  adb shell input tap 180 300
  sleep 1
  adb shell input text "$phone_number" # phone number
  sleep 1
  adb shell input tap 62 433
  sleep 1
  adb shell input tap 986 1458

  # Видаляємо тимчасовий файл
  adb shell rm "$temp_file"
}

function split_and_send {
  local phone_number="$1"
  local encoded_text="$2"


  # shellcheck disable=SC2155
  local decoded_text=$(echo "$encoded_text" | sed 's/%20/ /g')


  local chunk_size=100
  local current_chunk=""
  for word in $decoded_text; do
    if [ ${#current_chunk} -eq 0 ]; then
      current_chunk="$word"
    elif [ $((${#current_chunk} + ${#word} + 1)) -le $chunk_size ]; then
      current_chunk="$current_chunk $word"
    else


      # shellcheck disable=SC2155
      local encoded_chunk=$(echo "$current_chunk" | sed 's/ /%20/g')
      send_viber_message "$phone_number" "$encoded_chunk"
      current_chunk="$word"
    fi
  done


  if [ ${#current_chunk} -gt 0 ]; then

    # shellcheck disable=SC2155
    local encoded_chunk=$(echo "$current_chunk" | sed 's/ /%20/g')
    send_viber_message "$phone_number" "$encoded_chunk"
  fi
}

function message_converter {
  local input="$1"

  local url_message="${input// /%20}"
  echo "$url_message"
}


messenger_type="$1"
phone_number="$2"
message=$(message_converter "$3" )
display_mode="$4"

display_setting "$display_mode"

case $messenger_type in
    "WhatsApp")
        adb logcat -c
        adb shell am start -a android.intent.action.VIEW -d "https://wa.me/${phone_number}" > /dev/null
        sleep 4
        whatsappError=$(adb logcat -d | grep -i "Displayed com.whatsapp/.Conversation")
        if [[ -n $whatsappError ]] ; then
            stop_app com.whatsapp
            send_message_whatsapp "$phone_number" "$message"
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
        adb shell am start -a android.intent.action.VIEW -d viber://chat?number="${phone_number}" com.viber.voip > /dev/null
        sleep 5
        viberError=$(adb shell logcat -d | grep -Eq "act=com.viber.voip.action.SYSTEM_DIALOG|(startActivityAsUser) (BAL_ALLOW_VISIBLE_WINDOW) result code=0" && echo "Found")

        if [[ -z $viberError ]] ; then
            stop_app com.viber.voip
            split_and_send "$phone_number" "$message"
            sleep 2
            stop_app com.viber.voip
            echo "Viber"
        else
            stop_app com.viber.voip
            echo "VIerrorEvent"
        fi
        ;;

    "SMS")
        adb shell am start -a android.intent.action.SENDTO -d "sms://${phone_number}?body=${message}" > /dev/null
        sleep 2
        adb shell input tap 980 2200
        sleep 2
        adb shell am force-stop com.google.android.apps.messaging
        echo "SMS"
        ;;

    *)
        echo "Invalid messenger type"
        exit 1
        ;;
esac