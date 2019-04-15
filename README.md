# repro_130397466

A sample project to reproduce an issue with usage of restricted interface `Landroid/view/View;->mViewFlags:I` in `BottomNavigationView` in Android Q Beta 2.

Repro Steps:
1. Use Android Studio 3.3.2
2. Build and run project to Android Q Emulator
3. Tap on the second tab

ref:  
https://issuetracker.google.com/issues/130397466  
https://developer.android.com/preview/non-sdk-q#greylist-now-restricted
