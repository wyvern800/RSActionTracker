![Using ranged actionbar](https://i.imgur.com/i6dTwPo.gif)

# RSActionLogger
Basic system used to log your actions during your RS3 boss encounters (to show for your stream viewers what are you pressing) that was created by me for personal purposes, but as I showed people off, they started asking me for releasing, so thats it, I hope you guys have a good use on your stream!

**NOTE: This is not a keylogger, which means the action logging will only be used to process image drawing on the application, and also this will not steal your game informations, or any other personal info, code is OpenSourced so you guys can read and warrantly this is not a phishing/scamming software**.


------------


### Features
- **Easy Configurable** - You can have pretty much every key combination as this uses **jNativeHook**;
- **Works in Background** - You capture the window with OBS, then leave it open in another screen or hidden;
- **OpenSource** - Which means you can help me improving the code but not redistribute it;
- Support HEX color codes for the abilities borders.


------------


**Table of Contents**

- [Setup](#setup)
  * [First Steps](#first-steps)
    + [How do I add more Actions to the ActionList](#how-do-i-add-more-actions-to-the-actionlist)
    + [How do I add more ActionTiers](#how-do-i-add-more-actiontiers)
    + [How do I add more ActionStyles](#how-do-i-add-more-actionstyles)
- [Future implementations](#future-implementations)
  * [Credits](#credits)

## Setup
You will need a JDK 8x installed in your development, an IDE, I would recommend **IntelliJ Community** but you can use **Eclipse IDE** incase you want, both are free.

#### First steps
1. Import the project in any IDE, and add the jNativeHook to the libraries if you're having issues related to imports.
2. The main file for loading the application is **src.sagacity.Main.java**
3. Open it up and set a combat style, there are three atm: **ActionStyle.MELEE, ActionStyle.RANGED and ActionStyle.MAGIC**
4. After selecting a combat style, run the file  **Main.java**.
5. If the application is open, when pressing any key, you'l be receiving a console print saying you're on idle mode, press F12 to toggle between idle mode/combat mode. (**NOTE: idle mode is for when you're talking, and doing other things instead combatting, then the keys won't be triggered**)

------------

#### How do I add more Actions to the ActionList?
1. Go to the file ActionList.java(**src.objects.ActionList.java**)
2. And add this line edited as you want:
```java
ABILITY_NAME("ability name prettyfied", NativeKeyEvent.VC_1, false, false, false,
            ActionTier.BASIC_ABILITY, STRENGTH_PATH+"/fury.png", ActionStyle.MELEE),
```

##### Parameters:  
| enum            | String          | [NativeKeyEvent](https://javadoc.io/static/com.1stleg/jnativehook/2.0.3/org/jnativehook/keyboard/NativeKeyEvent.html "See all keyevents here")  | boolean         |    boolean      |      boolean    |    ActionTier   | String | ActionStyle |
| --------------- | --------------- | --------------- | --------------- | --------------- | --------------- | --------------- | --------------- |--------------- |
| ABILITY_NAME    | Corruption Shot |NativeKeyEvent.VC_1| false| false | false | ActionTier.BASIC_ABILITY | STRENGTH_PATH+"/fury.png"| ActionStyle.MELEE|

- **ABILITY_NAME:** | Used for enumeration purposes only (can be anything).  
- **1st param:** *String*  | used for the ability name showed on screen, Example: Corruption Shot.  
- **2nd param:** *NativeKeyEvent*  | used to determine which key we are pressing to trigger the event. [See all keyevents here](https://javadoc.io/static/com.1stleg/jnativehook/2.0.3/org/jnativehook/keyboard/NativeKeyEvent.html "See all keyevents here")  
- **3rd param:** *boolean*  | **True** if key uses CTRL mask (if the key is combined with CTRL), **False** if not (singular key pressing. Example. K)  
- **4th param:** *boolean*  | **True** if key uses SHIFT mask (if the key is combined with SHIFT, Example: SHIFT+K), **False** if not.  
- **5th param:** *boolean*  | **True** if key uses ALT mask (if the key is combined with ALT, Example: ALT+K) | **False** if not.  
- **6th param:** *ActionTier*  | The tier of the ability, there are currently 7 default tiers, that can be used:  
```java
 CONSUMABLE(0, "#9500ff"),
            BASIC_ABILITY( 1, "#ad4800"),
                TRESHOLD_ABILITY(2, "#1b9400"),
                        ULTIMATE_ABILITY(3, "#ebac00"),
                            DEFENSIVE_ABILITY(3, "white"),
                                    PRAYER( 4, "#7e8ec8"),
                                            WEAPON_SPEC(4, "#bd0026");
```
Choose one, in this example we're using: ActionTier.BASIC_ABILITY, you can leave as that to test it.  
- **7th param:** *String*  | The path to the action image we're using, in the example case we're using STRENGTH_PATH+**fury.png** - **NOTE:** The *STRENGTH_PATH*  in this example is just a constant so we dont type the full directory to where the image is, you can use any other directory from your computer, i'd strongy recommend using the same folder directory to avoid fail when loading the files.  
- **8th param:** *ActionStyle*  | The action style used by this action, used to determine if the abiilty we're using is the same of the **COMBAT_STYLE** we setted up when loading our application.  Which means if we set the var. to** ActionStyle.RANGED**, only abilities we added to the list that contains **RANGED action style** will be triggered when pressing the keys. 

**NOTE:** If the ability is wheter a defensive ability or consumable/items action that doesn't differ in any rotation, you must use **ActionStyle.NONE** as the last parameter, meaning the action can be triggered by any **COMBAT_STYLE**.  
  
Now save it up, and reload the application, if you don't know how to, go to the Setup section in this document.

------------

#### How do I add more ActionTiers?
1. **NOTE:**  This is not needed as the tiers are already done, just incase you want.
1. Go to the file ActionTier.java (**src.objects.ActionTier.java**)
2. And edit the lines as you want.
```java
CONSUMABLE(0, "#9500ff"),
            BASIC_ABILITY( 1, "#ad4800"),
                TRESHOLD_ABILITY(2, "#1b9400"),
                        ULTIMATE_ABILITY(3, "#ebac00"),
                            DEFENSIVE_ABILITY(3, "white"),
                                    PRAYER( 4, "#7e8ec8"),
                                            WEAPON_SPEC(4, "#bd0026");
```
------------

#### How do I add more ActionStyles?
1. **NOTE:**  This is not needed as the tiers are already done, just incase you want.
1. Go to the file ActionTier.java (**src.objects.ActionStyle.java**)
2. And edit the lines as you want.

```java
NONE(0),
        MELEE(1),
            RANGED(2),
                MAGIC(3);
```
------------

## Future implementations
- [ ] UI for actions adding/removing (instead of plain coding)
- [ ] Upgraded version with more functionalities
    - [ ] Node.js API for easier management
    - [ ] Menu with settings

------------

Thanks for reading until here, this took sometime and I hope you enjoy using my software 💖

Incase you like, please give me a Star here!

### Credits
[jNativeHook](https://github.com/kwhat/jnativehook/) - for the Keyboard keys listening.

