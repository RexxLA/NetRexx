cp "$*" /sdcard/
am start -a android.intent.action.VIEW  -d "file:///sdcard/""$*"
