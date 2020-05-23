# What's App Notifications in the Pixel 4 

Currently I have a problem with the notifications in What's App on my Pixel 4.

No matter how many times I set the notifications I receive to "Silent", they keep coming back as "Alert".

This means that I cannot turn the vibration off for these notifications if my phone is not in Silent Mode. 

Since I don't want to put my phone in Silent, and I don't want it to vibrate on what's app notifications, I tried to find the origin of this bug. 

## TL;DR

Every Time the user changes the Message Notification type (Alert|Silent) What's App creates a new channel for the notifications, named: `individual_chat_defaults_XX` (XX being a number incrementing).


## Related

I think the problem is the one in [this issue at Pixel Phone Help](https://support.google.com/pixelphone/thread/2935260?hl=en)

## Notifications in Android 10

As I understand it, for each notification in Android 10, there's an accompanying `channel_id` that specifies the type of notifications.

This allows us to apply settings to only some of the notifications types. 

Settings such as "No more notification of this type" or "Alert me on this type of notification".

## What you'll find in this repository

I made an app that creates a new channel every time it creates a new notification, but with the same name.

This means that it will keep vibrating if your phone is in vibrate mode.

If you add access to all notifications to this app, it will log channel ids for all notifications you receive and you can follow What's App channel ids. 

## Devices with the bug

_Feel free to make a PR with your phone it shows the bug also_

- Pixel 4 / Android 10
- Pixel 3a / Android 10

## How to reproduce

Set your phone to vibration mode.

Start the app with Android Studio.

Push the Floating Action Button, your phone shoul vibrate. 

Long press on the notification should allow you to make it silent.

Press again on the FAB.

It should vibrate again, with the same channel name.



## Analysis

The main problem is that:

The app creates a notification, with a new `channel_id: "ab1", name:"hello"`. 

_`individual_chat_defaults` in What's App (I don't know the original channel in WA, but it's `individual_chat_defaults_30` on my phone.)_

The user sets that channel to "silent" 

_At this point a new notification channel `individual_chat_defaults_01` is created in What's App, I don't know how they get that event. But they keep increasing the number each time._

_I suspect that What's app listens to these kind of events to know when you set a different notification type for a contact. In this case they need to create a new notification channel for this contact_

_To reproduce that behaviour, in the app I simply create a new channel `channel_id: "ab2", name:"hello"` and remove the previous one (`channel_id: "ab1", name:"hello"`)._

On the next notification, since it is a new channel, the "Alert" (default) mode is enabled.

But looking at the notifications, I still see the "hello" channel, which is very confusing.


## What I think

- First of all What's App should remove that behaviour, because it is not UX friendly at all.
- Then Android should not let developers create channels with differents Ids but the same Names, because they're, as stated in [DDD](https://en.wikipedia.org/wiki/Domain-driven_design#Building_blocks) VALUES and not ENTITIES.

The User doesn't want to silent the channel with the id `ab2`, but the channel with the name `hello`. 

## Additional Info

In contacted What's App support, they told me it was a bug in my device (as they told to someone in the Google Pixel Support)

The app is the simplest one I could do and I'm no expert in Android.

I don't really know what to do with all that, so leaving it to you guys.

Please, feel free to open a PR for anything in this repository (even typos, english is not my first language).

For the NotificationListener part, the code is from [this repository](https://github.com/kpbird/NotificationListenerService-Example), thanks to @kpbird. 