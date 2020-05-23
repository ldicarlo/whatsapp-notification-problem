



# What's App Notifications in the Pixel 4 

Currently I have a problem with the notifications in What's App on my Pixel 4.
No matter how many times I set the notifications I receive to "Silent", they keep coming back as "Alert".
This means that I cannot turn the vibration off for these notifications if my phone is not in Silent Mode. 

Since I don't want to put my phone in Silent, and I don't want it to vibrate on what's app notifications, I tried to find the origin of this bug. 

## TL;DR

I think What's App removes the notification channel each it does a new notification. 

## Notifications in Android 10

As I understand it, for each notification in Android 10, there's an accompanying `channel_id` that specifies the type of notifications.
This allows us to apply settings to only some of the notifications types. 
Settings such as "No more notification of this type" or "Alert me on this type of notification"

## Analysis

I reproduced that problem on my phone with an Android app. 
The main problematic part is as follow:

The app creates a notification, with a new `channel_id: "hello"`
