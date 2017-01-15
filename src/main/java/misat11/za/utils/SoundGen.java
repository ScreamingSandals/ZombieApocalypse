
package misat11.za.utils;

import org.bukkit.Sound;

import misat11.za.Main;

public class SoundGen {

  public static Sound get(String v18, String v19) {
    Sound finalSound = null;

    try {
      if (Main.s_version.startsWith("v1_8")) {
        finalSound = Sound.valueOf(v18);
      } else {
        finalSound = Sound.valueOf(v19);
      }
    } catch (Exception ex) {
    	
    }

    return finalSound;
  }

}