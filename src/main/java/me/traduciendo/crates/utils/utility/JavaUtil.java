package me.traduciendo.crates.utils.utility;

import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.time.DurationFormatUtils;

public final class JavaUtil {
   public static Integer tryParseInt(String string) {
      try {
         return Integer.parseInt(string);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public static String formatDurationInt(int input) {
      return DurationFormatUtils.formatDurationWords((long)input * 1000L, true, true);
   }

   public static String formatDurationLong(long input) {
      return DurationFormatUtils.formatDurationWords(input, true, true);
   }

   public static String formatLongMin(long time) {
      long totalSecs = time / 1000L;
      return String.format("%02d:%02d", totalSecs / 60L, totalSecs % 60L);
   }

   public static String formatLongHour(long time) {
      long totalSecs = time / 1000L;
      long seconds = totalSecs % 60L;
      long minutes = totalSecs % 3600L / 60L;
      long hours = totalSecs / 3600L;
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
   }

   public static long formatLong(String input) {
      if (input != null && !input.isEmpty()) {
         long result = 0L;
         StringBuilder number = new StringBuilder();

         for(int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
               number.append(c);
            } else {
               String str;
               if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                  result += convertLong(Integer.parseInt(str), c);
                  number = new StringBuilder();
               }
            }
         }

         return result;
      } else {
         return -1L;
      }
   }

   private static long convertLong(int value, char unit) {
      switch(unit) {
      case 'M':
         return (long)value * TimeUnit.DAYS.toMillis(30L);
      case 'd':
         return (long)value * TimeUnit.DAYS.toMillis(1L);
      case 'h':
         return (long)value * TimeUnit.HOURS.toMillis(1L);
      case 'm':
         return (long)value * TimeUnit.MINUTES.toMillis(1L);
      case 's':
         return (long)value * TimeUnit.SECONDS.toMillis(1L);
      case 'y':
         return (long)value * TimeUnit.DAYS.toMillis(365L);
      default:
         return -1L;
      }
   }

   public static int formatInt(String input) {
      if (input != null && !input.isEmpty()) {
         int result = 0;
         StringBuilder number = new StringBuilder();

         for(int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
               number.append(c);
            } else {
               String str;
               if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                  result += convertInt(Integer.parseInt(str), c);
                  number = new StringBuilder();
               }
            }
         }

         return result;
      } else {
         return -1;
      }
   }

   private static int convertInt(int value, char unit) {
      switch(unit) {
      case 'd':
         return value * 60 * 60 * 24;
      case 'h':
         return value * 60 * 60;
      case 'm':
         return value * 60;
      case 's':
         return value;
      default:
         return -1;
      }
   }

   private JavaUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
