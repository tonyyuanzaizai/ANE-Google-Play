Air Native Extension for GooglePlayService (Android)
======================================

This is an [Air native extension](http://www.adobe.com/devnet/air/native-extensions-for-air.html) for [Facebook SDK](https://developers.facebook.com/docs#apis-and-sdks) on iOS and Android. It has been originally developed by [FreshPlanet](http://freshplanet.com). I will try to maintain this project. For any suggestions open a issue.

GooglePlayService SDK Versions
---------

* Android: 9.4.0

**IMPORTANT NOTE:** Please use -swf-version=29 and AIR >= 18 when you use this ANE otherwise you can have some issues. 

**IMPORTANT NOTE FOR ANDROID:** Use this ANE only when you build with AIR >=18. New FB Android SDK needs Java 1.7 and extension compiled with Java 1.7 will not work with AIR 16 (AIR 17 had a lot of issues with Android so I don't consider it either). Context of extension will not be created on Android. There should be no issues with iOS.

Installation
---------

The ANE binary (AirGooglePlayService.ane) is located in the *bin* folder. You should add it to your application project's Build Path and make sure to package it with your app (more information [here](http://help.adobe.com/en_US/air/build/WS597e5dadb9cc1e0253f7d2fc1311b491071-8000.html)).


On Android:

* you will need to add the following activities and permission in your application descriptor:

```xml
<android>
    <manifestAdditions><![CDATA[
        <manifest android:installLocation="auto">
            
            ...

            <uses-permission android:name="android.permission.INTERNET"/>
            
            ...

            <application>

                ...
				
				<!-- GooglePlay Games Services 311712923402 -->
				<meta-data android:name="com.google.android.gms.games.APP_ID" android:value="\ 311712923402" />
				<meta-data android:name="com.google.android.gms.appstate.APP_ID" android:value="\ 311712923402" />
				<meta-data android:name="com.google.android.gms.version" android:value="9452000"/>
				<activity android:name="com.freshplanet.googleplaygames.MainActivity" android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
                
            </application>

        </manifest>
    ]]></manifestAdditions>
</android>
```

**NOTE:** It is important to prefix YOUR_FB_APP_ID with "fb" in `<meta-data>` (and ONLY in `<meta-data>`) tag, because of bug in Android manifest file (http://stackoverflow.com/questions/16156856/android-facebook-applicationid-cannot-be-null). Facebook SDK code in this ANE was modified to recognize FB_APP_ID prefixed with "fb".

Documentation
--------

ActionScript documentation is available in HTML format on project pages [here](http://nodrock.github.io/ANE-Facebook/docs/).


Samples (deprecated)
--------

A sample project is available in the *sample* folder.
Read HOW-TO.txt walkthrought to set-up and run the sample application.


Build from source
---------

Should you need to edit the extension source code and/or recompile it, you will find an ant build script (build.xml) in the *build* folder:
    
```bash
cd /path/to/the/ane

# Setup build configuration
cd build
mv example.build.config build.config
# Edit build.config file to provide your machine-specific paths

# Build the ANE
ant
```

**NOTE (deprecated):**  You MUST use Java 1.6 otherwise in android context will be null (probably bug in Adobe AIR SDK). On OSX you can call "export JAVA_HOME=`/usr/libexec/java_home -v 1.6`" without " to set JAVA_HOME properly.

Authors
------

This ANE has been originally written by [Thibaut Crenn](https://github.com/titi-us), [Alexis Taugeron](http://alexistaugeron.com) and [Renaud Bardet](http://github.com/renaudbardet). Rewrites and modifications to version SDK 4.x were made by [Ján Horváth](https://github.com/nodrock).

