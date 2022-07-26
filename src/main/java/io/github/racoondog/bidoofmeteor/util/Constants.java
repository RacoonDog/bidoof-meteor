package io.github.racoondog.bidoofmeteor.util;

import it.unimi.dsi.fastutil.chars.Char2CharMap;
import it.unimi.dsi.fastutil.chars.Char2CharOpenHashMap;
import meteordevelopment.meteorclient.utils.PreInit;

public class Constants {
    public static final Char2CharMap FANCY = new Char2CharOpenHashMap();

    @PreInit
    public static void init() {
        String[] a = "abcdefghijklmnopqrstuvwxyz".split("");
        String[] b = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴩqʀꜱᴛᴜᴠᴡxyᴢ".split("");
        for (int i = 0; i < a.length; i++) FANCY.put(a[i].charAt(0), b[i].charAt(0));
    }
}
