package io.github.racoondog.bidoofmeteor.impl;

import io.github.racoondog.bidoofmeteor.modules.ChatEmotes;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChatEmotesImpl {
    public static String emoteReplacer(String msg) {
        return emoteReplacer(msg, 0);
    }

    //This is horrible
    private static String emoteReplacer(String msg, int start) {
        int idx = msg.indexOf(':', start);
        if (idx < 0) return msg;
        String next = msg.substring(idx + 1);
        int idx2 = next.indexOf(':', 1);
        if (idx2 < 0 || idx2 > 16) return msg;
        String segment = next.substring(0, idx2);
        StringBuilder sb = new StringBuilder().append(msg, 0, idx);
        ChatEmotes chatEmotes = Modules.get().get(ChatEmotes.class);
        boolean variations = chatEmotes.variations.get();
        if (chatEmotes.minecraft.get()) {
            switch (segment) {
                case "sword" -> {
                    return finalize(sb, "🗡", next, idx2);
                }
                case "shield" -> {
                    return finalize(sb, "🛡", next, idx2);
                }
                case "axe" -> {
                    return finalize(sb, "🪓", next, idx2);
                }
                case "bow" -> {
                    return finalize(sb, "🏹", next, idx2);
                }
                case "trident" -> {
                    return finalize(sb, "🔱", next, idx2);
                }
                case "rod" -> {
                    return finalize(sb, "🎣", next, idx2);
                }
                case "potion" -> {
                    return finalize(sb, "🧪", next, idx2);
                }
                case "fire" -> {
                    return finalize(sb, "🔥", next, idx2);
                }
                case "shears" -> {
                    return finalize(sb, '✂', next, idx2);
                }
                case "pick" -> {
                    return finalize(sb, '⛏', next, idx2);
                }
                case "lightning" -> {
                    return finalize(sb, '⚡', next, idx2);
                }
                case "bell" -> {
                    return finalize(sb, "🔔", next, idx2);
                }
                case "crossed_swords" -> {
                    return finalize(sb, '⚔', next, idx2);
                }
                case "meteor" -> {
                    return finalize(sb, '☄', next, idx2);
                }
                case "cloud" -> {
                    return finalize(sb, '☁', next, idx2);
                }
                case "meat" -> {
                    return finalize(sb, "🍖", next, idx2);
                }
                case "snowman" -> {
                    return finalize(sb, '☃', next, idx2);
                }
                case "rain" -> {
                    return finalize(sb, "🌧", next, idx2);
                }
            }
        }
        if (chatEmotes.alphabet.get()) {
            switch (segment) {
                case "1" -> {
                    return finalize(sb, '➀', next, idx2);
                }
                case "2" -> {
                    return finalize(sb, '➁', next, idx2);
                }
                case "3" -> {
                    return finalize(sb, '➂', next, idx2);
                }
                case "4" -> {
                    return finalize(sb, '➃', next, idx2);
                }
                case "5" -> {
                    return finalize(sb, '➄', next, idx2);
                }
                case "6" -> {
                    return finalize(sb, '➅', next, idx2);
                }
                case "7" -> {
                    return finalize(sb, '➆', next, idx2);
                }
                case "8" -> {
                    return finalize(sb, '➇', next, idx2);
                }
                case "9" -> {
                    return finalize(sb, '➈', next, idx2);
                }
                case "10" -> {
                    return finalize(sb, '➉', next, idx2);
                }
                case "A" -> {
                    return finalize(sb, 'Ⓐ', next, idx2);
                }
                case "B" -> {
                    return finalize(sb, 'Ⓑ', next, idx2);
                }
                case "C" -> {
                    return finalize(sb, 'Ⓒ', next, idx2);
                }
                case "D" -> {
                    return finalize(sb, 'Ⓓ', next, idx2);
                }
                case "E" -> {
                    return finalize(sb, 'Ⓔ', next, idx2);
                }
                case "F" -> {
                    return finalize(sb, 'Ⓕ', next, idx2);
                }
                case "G" -> {
                    return finalize(sb, 'Ⓖ', next, idx2);
                }
                case "H" -> {
                    return finalize(sb, 'Ⓗ', next, idx2);
                }
                case "I" -> {
                    return finalize(sb, 'Ⓘ', next, idx2);
                }
                case "J" -> {
                    return finalize(sb, 'Ⓙ', next, idx2);
                }
                case "K" -> {
                    return finalize(sb, 'Ⓚ', next, idx2);
                }
                case "L" -> {
                    return finalize(sb, 'Ⓛ', next, idx2);
                }
                case "M" -> {
                    return finalize(sb, 'Ⓜ', next, idx2);
                }
                case "N" -> {
                    return finalize(sb, 'Ⓝ', next, idx2);
                }
                case "O" -> {
                    return finalize(sb, 'Ⓞ', next, idx2);
                }
                case "P" -> {
                    return finalize(sb, 'Ⓟ', next, idx2);
                }
                case "Q" -> {
                    return finalize(sb, 'Ⓠ', next, idx2);
                }
                case "R" -> {
                    return finalize(sb, 'Ⓡ', next, idx2);
                }
                case "S" -> {
                    return finalize(sb, 'Ⓢ', next, idx2);
                }
                case "T" -> {
                    return finalize(sb, 'Ⓣ', next, idx2);
                }
                case "U" -> {
                    return finalize(sb, 'Ⓤ', next, idx2);
                }
                case "V" -> {
                    return finalize(sb, 'Ⓥ', next, idx2);
                }
                case "W" -> {
                    return finalize(sb, 'Ⓦ', next, idx2);
                }
                case "X" -> {
                    return finalize(sb, 'Ⓧ', next, idx2);
                }
                case "Y" -> {
                    return finalize(sb, 'Ⓨ', next, idx2);
                }
                case "Z" -> {
                    return finalize(sb, 'Ⓩ', next, idx2);
                }
                case "a" -> {
                    return finalize(sb, 'ⓐ', next, idx2);
                }
                case "b" -> {
                    return finalize(sb, 'ⓑ', next, idx2);
                }
                case "c" -> {
                    return finalize(sb, 'ⓒ', next, idx2);
                }
                case "d" -> {
                    return finalize(sb, 'ⓓ', next, idx2);
                }
                case "e" -> {
                    return finalize(sb, 'ⓔ', next, idx2);
                }
                case "f" -> {
                    return finalize(sb, 'ⓕ', next, idx2);
                }
                case "g" -> {
                    return finalize(sb, 'ⓖ', next, idx2);
                }
                case "h" -> {
                    return finalize(sb, 'ⓗ', next, idx2);
                }
                case "i" -> {
                    return finalize(sb, 'ⓘ', next, idx2);
                }
                case "j" -> {
                    return finalize(sb, 'ⓙ', next, idx2);
                }
                case "k" -> {
                    return finalize(sb, 'ⓚ', next, idx2);
                }
                case "l" -> {
                    return finalize(sb, 'ⓛ', next, idx2);
                }
                case "m" -> {
                    return finalize(sb, 'ⓜ', next, idx2);
                }
                case "n" -> {
                    return finalize(sb, 'ⓝ', next, idx2);
                }
                case "o" -> {
                    return finalize(sb, 'ⓞ', next, idx2);
                }
                case "p" -> {
                    return finalize(sb, 'ⓟ', next, idx2);
                }
                case "q" -> {
                    return finalize(sb, 'ⓠ', next, idx2);
                }
                case "r" -> {
                    return finalize(sb, 'ⓡ', next, idx2);
                }
                case "s" -> {
                    return finalize(sb, 'ⓢ', next, idx2);
                }
                case "t" -> {
                    return finalize(sb, 'ⓣ', next, idx2);
                }
                case "u" -> {
                    return finalize(sb, 'ⓤ', next, idx2);
                }
                case "v" -> {
                    return finalize(sb, 'ⓥ', next, idx2);
                }
                case "w" -> {
                    return finalize(sb, 'ⓦ', next, idx2);
                }
                case "x" -> {
                    return finalize(sb, 'ⓧ', next, idx2);
                }
                case "y" -> {
                    return finalize(sb, 'ⓨ', next, idx2);
                }
                case "z" -> {
                    return finalize(sb, 'ⓩ', next, idx2);
                }
            }
        }
        if (chatEmotes.music.get()) {
            switch (segment) {
                case "eighth" -> {
                    return finalize(sb, '♪', next, idx2);
                }
                case "quarter" -> {
                    return finalize(sb, '♩', next, idx2);
                }
                case "beamed_eighth" -> {
                    return finalize(sb, '♫', next, idx2);
                }
                case "beamed_sixteenth" -> {
                    return finalize(sb, '♬', next, idx2);
                }
                case "flat" -> {
                    return finalize(sb, '♭', next, idx2);
                }
            }
        }
        if (chatEmotes.symbols.get()) {
            switch (segment) {
                case "scorpius" -> {
                    return finalize(sb, '♏', next, idx2);
                }
                case "aquarius" -> {
                    return finalize(sb, '♒', next, idx2);
                }
                case "aries" -> {
                    return finalize(sb, '♈', next, idx2);
                }
                case "mercury" -> {
                    return finalize(sb, '☿', next, idx2);
                }
                case "wheelchair" -> {
                    return finalize(sb, '♿', next, idx2);
                }
                case "male" -> {
                    return finalize(sb, '♂', next, idx2);
                }
                case "female" -> {
                    return finalize(sb, '♀', next, idx2);
                }
                case "tm" -> {
                    return finalize(sb, '™', next, idx2);
                }
                case "registered" -> {
                    return finalize(sb, '®', next, idx2);
                }
                case "copyright" -> {
                    return finalize(sb, '©', next, idx2);
                }
                case "toxic" -> {
                    return finalize(sb, '☣', next, idx2);
                }
                case "yinyang" -> {
                    return finalize(sb, '☯', next, idx2);
                }
                case "peace" -> {
                    return finalize(sb, '☮', next, idx2);
                }
            }
            if (variations && segment.equals("peace2")) return finalize(sb, '✌', next, idx2);
        }
        if (chatEmotes.math.get()) {
            switch (segment) {
                case "diamond" -> {
                    return finalize(sb, '⋄', next, idx2);
                }
                case "dot" -> {
                    return finalize(sb, '⋅', next, idx2);
                }
                case "division_times" -> {
                    return finalize(sb, '⋇', next, idx2);
                }
            }
        }
        if (chatEmotes.emoticons.get()) {
            switch (segment) {
                case "smile" -> {
                    return finalize(sb, '☺', next, idx2);
                }
                case "sad" -> {
                    return finalize(sb, '☹', next, idx2);
                }
                case "skull" -> {
                    return finalize(sb, '☠', next, idx2);
                }
                case "point_up" -> {
                    return finalize(sb, '☝', next, idx2);
                }
                case "point_down" -> {
                    return finalize(sb, '☟', next, idx2);
                }
                case "writing" -> {
                    return finalize(sb, '✍', next, idx2);
                }
                case "point_right" -> {
                    return finalize(sb, '☞', next, idx2);
                }
                case "point_left" -> {
                    return finalize(sb, '☜', next, idx2);
                }
            }
            if (variations) {
                switch (segment) {
                    case "smile2" -> {
                        return finalize(sb, '☻', next, idx2);
                    }
                    case "smile3" -> {
                        return finalize(sb, 'ツ', next, idx2);
                    }
                    case "point_left2" -> {
                        return finalize(sb, '☚', next, idx2);
                    }
                    case "point_right2" -> {
                        return finalize(sb, '☛', next, idx2);
                    }
                }
            }
        }
        if (chatEmotes.games.get()) {
            switch (segment) {
                case "black_king" -> {
                    return finalize(sb, '♔', next, idx2);
                }
                case "black_queen" -> {
                    return finalize(sb, '♕', next, idx2);
                }
                case "white_king" -> {
                    return finalize(sb, '♚', next, idx2);
                }
                case "white_queen" -> {
                    return finalize(sb, '♛', next, idx2);
                }
                case "spade" -> {
                    return finalize(sb, '♠', next, idx2);
                }
                case "spade2" -> {
                    return finalize(sb, '♤', next, idx2);
                }
                case "club" -> {
                    return finalize(sb, '♣', next, idx2);
                }
                case "club2" -> {
                    return finalize(sb, '♧', next, idx2);
                }
            }
        }
        if (chatEmotes.miscellanious.get()) {
            switch (segment) {
                case "heart" -> {
                    return finalize(sb, '❤', next, idx2);
                }
                case "star" -> {
                    return finalize(sb, '★', next, idx2);
                }
                case "flower" -> {
                    return finalize(sb, '❀', next, idx2);
                }
                case "cat" -> {
                    return finalize(sb, "ᓚᘏᗢ", next, idx2);
                }
                case "wave" -> {
                    return finalize(sb, "🌊", next, idx2);
                }
                case "sun" -> {
                    return finalize(sb, '☀', next, idx2);
                }
                case "pencil" -> {
                    return finalize(sb, '✎', next, idx2);
                }
                case "umbrella" -> {
                    return finalize(sb, '☂', next, idx2);
                }
                case "sparkle" -> {
                    return finalize(sb, '❈', next, idx2);
                }
                case "cross" -> {
                    return finalize(sb, '✖', next, idx2);
                }
                case "check" -> {
                    return finalize(sb, '✓', next, idx2);
                }
                case "scissors" -> {
                    return finalize(sb, '✄', next, idx2);
                }
                case "telephone" -> {
                    return finalize(sb, '☎', next, idx2);
                }
                case "phone" -> {
                    return finalize(sb, '✆', next, idx2);
                }
                case "mail" -> {
                    return finalize(sb, '✉', next, idx2);
                }
                case "black_square" -> {
                    return finalize(sb, '□', next, idx2);
                }
                case "white_square" -> {
                    return finalize(sb, '■', next, idx2);
                }
                case "triple_bar" -> {
                    return finalize(sb, '≡', next, idx2);
                }
                case "communism" -> {
                    return finalize(sb, '⚒', next, idx2);
                }
                case "quebec" -> {
                    return finalize(sb, '⚜', next, idx2);
                }
            }
            if (variations) {
                switch (segment) {
                    case "heart2" -> {
                        return finalize(sb, '❥', next, idx2);
                    }
                    case "heart3" -> {
                        return finalize(sb, '♥', next, idx2);
                    }
                    case "heart4" -> {
                        return finalize(sb, '♡', next, idx2);
                    }
                    case "heart5" -> {
                        return finalize(sb, '❣', next, idx2);
                    }
                    case "star2" -> {
                        return finalize(sb, '☆', next, idx2);
                    }
                    case "star3" -> {
                        return finalize(sb, '✮', next, idx2);
                    }
                    case "star4" -> {
                        return finalize(sb, '✪', next, idx2);
                    }
                    case "star5" -> {
                        return finalize(sb, '✦', next, idx2);
                    }
                    case "star6" -> {
                        return finalize(sb, '✧', next, idx2);
                    }
                    case "star7" -> {
                        return finalize(sb, '⋆', next, idx2);
                    }
                    case "flower2" -> {
                        return finalize(sb, '✿', next, idx2);
                    }
                    case "sun2" -> {
                        return finalize(sb, '☼', next, idx2);
                    }
                    case "cross2" -> {
                        return finalize(sb, '✗', next, idx2);
                    }
                    case "cross3" -> {
                        return finalize(sb, '✘', next, idx2);
                    }
                    case "cross4" -> {
                        return finalize(sb, '✕', next, idx2);
                    }
                    case "check2" -> {
                        return finalize(sb, '✓', next, idx2);
                    }
                    case "telephone2" -> {
                        return finalize(sb, '☏', next, idx2);
                    }
                }
            }
        }
        return fail(sb, segment, next, idx2);
    }

    private static String fail(StringBuilder sb, String segment, String str, int idx) {
        sb.append(':').append(segment).append(':');
        return finalize(sb, str, idx);
    }

    private static String finalize(StringBuilder sb, char c, String str, int idx) {
        sb.append(c);
        return finalize(sb, str, idx);
    }

    private static String finalize(StringBuilder sb, String c, String str, int idx) {
        sb.append(c);
        return finalize(sb, str, idx);
    }

    private static String finalize(StringBuilder sb, String str, int idx) {
        String end = str.substring(idx + 1);
        int nextIndex = end.indexOf(':');
        return sb.append(nextIndex != -1 ? emoteReplacer(end, nextIndex) : end).toString();
    }
}
