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

message=${3}

case $1 in
	WhatsApp)
		## Write WA code flow
		# Clean logs
		adb logcat -c
		# Open WA chat contact and redirect output to dev/null
		adb shell am start -a android.intent.action.VIEW -d "https://wa.me/${2}" > /dev/null
		sleep 4
		waerror=$(adb logcat -d | grep "Displayed com.whatsapp/.Conversation")
		if [[ -n $waerror ]]
		then
			adb shell input tap 300 2200
			for var in ${message}
			do
				sleep 0.2
				adb shell input text ${var}
				adb shell input keyevent 62
			done
			adb shell input tap 1000 1500
			sleep 2
			adb shell am force-stop com.whatsapp
			echo WAEvent
		else
			stop_whatsapp
			echo WAerrorEvent
		fi
	;;
	Viber)
		## Write Viber code flow

		#Clean logcat logs
		adb logcat -c
		#Open viber chat with contact
		adb shell "am start -a android.intent.action.VIEW -d 'viber://chat?number=$2' com.viber.voip" > /dev/null
		#Waithing for 2 seconds for chat to open
		sleep 2
		#Check is user have viber
		error=$(adb logcat -d | grep "ViberSystemActivity")
		if [[ -z $error ]]
		then
			sleep 3
			adb shell input tap 300 2200
		for var in ${message}
		do
			sleep 0.1
			adb shell input text ${var}
			adb shell input keyevent 62
		done
		adb shell input tap 1000 1500
		sleep 2
		adb shell am force-stop com.viber.voip
		echo VIEvent
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

			adb shell input text ${var}
			adb shell input keyevent 62
		done
	       	sleep 1
		adb shell input tap 1000 1500
		sleep 3
		adb shell am force-stop com.google.android.apps.messaging
		echo SMSEvent
	;;
esac
