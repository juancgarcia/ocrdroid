# ocrdroid #

## A Titanium Module for Android that provides TessTwo (Tesseract) OCR support ##

### (this is still a work in progress) ###

This project depends on [tess-two](https://github.com/rmtheis/tess-two) which in turn depends on tesseract and leptonica.

Inspect *.classpath* and *build.properties* and edit the paths to match your environment.

Clone or copy tess-two, then you must decide between two options to proceed: (both methods require the Android NDK)

 - Build tess-two and manually copy the native libraries into **ocrdroid**
 - merge the java and c source into **ocrdroid** then let Titanium to perform the JNI compilation

### Titanium JNI compilation ###
>Copy tess-two's jni into **ocrdroid** and merge tess-two's src with **ocrdroid**'s existing src folder.

>Uncomment and edit the ndk variables in build.properties to point to your ndk installation.

### Build binaries separately ###
>Build Tesseract binaries using tess-two, follow the directions there.

>Then copy the binary folders (armeabi, armeabi-v7a, etc) to libs.import (targets defined in build.xml will copy them into the module during packaging)

>Add the tess-two.jar to **ocrdroid**'s classpath (I actually can't get the build to work adding the jar this way. It always fails in "cannot find symbol" on TessBaseAPI even though Titanium Studio shows no problems. My workaround is to copy the tess-two java source into **ocrdroid** and let Titanium compile it as part of **ocrdroid** )

### Common Setup ###
Next create an assets folder and include a zip file named "tess.zip" (this is hardcoded in OcrdroidModule.unpackTessData) containing the trained data filed inside of a tessdata folder as outlined in tess-two's readme 
