#!/bin/bash

# Firs we must validate first argument $1
# Supported messengers must be Viber/WhatsApp/SMS
# Second argument is phone number $2

function stop_viber(){

	adb shell am force-stop com.viber.voip
	sleep 0.5
	adb shell pkill -f com.viber.voip
}

function stop_whatsapp(){

	adb shell am force-stop com.whatsapp
	sleep 0.5
	adb shell pkill -f com.whatsapp
}

function display() {
    if [[ $dis_play == "start" ]]
      then
        # disable screen off while charging
        adb shell settings put global stay_on_while_plugged_in 3 > /dev/null
      else
        # enable screen off function
        adb shell settings delete global stay_on_while_plugged_in > /dev/null
    fi
}

message=${3}
dis_play=${4}

display

case $1 in
	WhatsApp)
		## Write WA code flow
		# Clean logs
		adb logcat -c
		# Open WA chat contact and redirect output to dev/null
		adb shell am start -a android.intent.action.VIEW -d "https://wa.me/${2}" > /dev/null
		sleep 4
		wa_error=$(adb logcat -d | grep "Displayed com.whatsapp/.Conversation")
		if [[ -n $wa_error ]]
		then
			adb shell input tap 300 2200
			for var in ${message}
			do
				sleep 0.2
				adb shell input text "${var}"
				adb shell input keyevent 62
			done
			adb shell input tap 1000 1500
			sleep 2
			adb shell am force-stop com.whatsapp
			echo WhatsApp
		else
			stop_whatsapp
			echo WAerrorEvent
		fi
	;;
	Viber)
		#Clean logcat logs
		adb logcat -c
		#Open viber chat with contact
		adb shell "am start -a android.intent.action.VIEW -d 'viber://chat?number=$2' com.viber.voip" > /dev/null
		#Waithing for 2 seconds for chat to open
		sleep 2
		#Check is user have viber
		error=$(adb logcat -d | grep "ViberSystemActivity")
		if [[ -n $error ]]
		then
			sleep 3
			adb shell input tap 300 2200
		for var in ${message}
		do
			sleep 0.1
			adb shell input text "${var}"
			adb shell input keyevent 62
		done
		adb shell input tap 1000 1500
		sleep 2
		adb shell am force-stop com.viber.voip
		echo Viber
		else
			stop_viber
			echo VIerrorEvent
		fi
	;;
	SMS)

		## Write  SMS Workflow
		adb shell "am start -a android.intent.action.SENDTO -d sms:$2" > /dev/null
		sleep 2
		adb shell input tap 400 2200
		sleep 1
		for var in ${message}
		do

			adb shell input text "${var}"
			adb shell input keyevent 62
		done
	       	sleep 1
		adb shell input tap 1000 1500
		sleep 3
		adb shell am force-stop com.google.android.apps.messaging
		echo SMS
	;;
esac
