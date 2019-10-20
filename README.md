# se206VARpedia
SE206 2019 Design Project

Beta version of VARPedia - a multimedia interface to Wikipedia.

Easy-to-run method (WORKS ON BETA LAB IMAGE WITH JAVA 8)

1. Ensure VARpedia_VM.sh, bin, src, flickr-api-keys.txt are on the same "level" in the directory hierarchy
2. Enter your own Flickr API key into flick-api-keys.txt
3. In the terminal, cd to the directory of files
4. Execute the script: ./VARpedia.sh
5. If this doesn't work, refer to step 7 of the IDE method

Run in IDE method (best way to test in both image and lab beta image)

1. Add project source to IDE
2. Ensure use of Java 8
3. Ensure Javafx is added/in jre or as a library
4. Add the libs folder as a library, it is the library for the flickr API
5. Enter your own Flickr API key into the txt file flick-api-keys.txt
6. Now that all these are checked off, the program should now be runnable
7. If in the case you are using Java 11 or above, please go to the files RingProgressIndicator.java and ProgressCircleIndicator.java, and change import com.sun.javafx.css.converters.* to import javafx.css.converter.SizeConverter;

Bugs we are aware of:

* previewing certain sentences with the NZ Male voice won't work
* Creations with only 1 image only work properly when used with 1 chunk
* short creations (e.g. 1 second) may not contain over 8-9 images
* Application freezes slightly when beginning from title page, this is due to the next scene being very large

__ATTRIBUTIONS:__

https://creativecommons.org/licenses/by/3.0/nz/

_WITH MODIFICATIONS:_

Wiki logo - uploaded by Anomie from https://en.wikipedia.org/wiki/File:Wikipedia-logo-v2.svg
Licensed under the Creative Commons Attribution-ShareAlike 3.0 license.

Play Icon - made by Celcius Creative from https://www.iconfinder.com/icons/3695059/music_play_play_button_player_icon Licensed under a Creative Commons Attribution (3.0) license.

Volume icon - Icon made by [Freepik] from www.flaticon.com

Mute icon - Icon made by [Freepik] from www.flaticon.com

_MUSIC_

Silence Violin by KhalafNasirs (c) copyright 2019 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/khalafnasirs/59559 

Happy Face by KhalafNasirs (c) copyright 2019 Licensed under a Creative Commons Attribution Noncommercial  (3.0) license. http://dig.ccmixter.org/files/khalafnasirs/59479 

Cold Pimp Man by Martijn de Boer (NiGiD) (c) copyright 2017 Licensed under a Creative Commons Attribution Noncommercial  (3.0) license. http://dig.ccmixter.org/files/NiGiD/56200 

_CODE_

skins.progressindicator - "fx-progress-circle" made by torakiki from https://github.com/torakiki/fx-progress-circle
Licensed under an Apache 2.0 license