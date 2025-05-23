package me.traduciendo.crates.airdrops.util.chat;


import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

public class Symbols {

    public static String HEALTH = StringEscapeUtils.unescapeJava("\u2764");
    public static String ARROW_LEFT = StringEscapeUtils.unescapeJava("\u00AB");
    public static String ARROW_RIGHT = StringEscapeUtils.unescapeJava("\u00BB");
    public static String X = ChatColor.AQUA + StringEscapeUtils.unescapeJava("\u2716");
    public static String ALERT = StringEscapeUtils.unescapeJava("\u26A0");
    public static String SETTINGS_PREFIX = StringEscapeUtils.unescapeJava(" &9&l\u2503 \u00BB ");
    public static String SETTINGS_PREFIX_EMPTY = "      ";
}