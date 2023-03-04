package net.devscape.project.guilds.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputChecker
{
    public static boolean noSpecialCharacters(final String[] args) {
        for (final String arg : args) {
            final Pattern pattern = Pattern.compile("^a-ZA-Z0-9");
            final Matcher matcher = pattern.matcher(arg);
            if (matcher.find()) {
                return false;
            }
        }
        return true;
    }
}
