package sancho.view.utility;

public class Base64 {
   public static String encode(byte[] data) {
      StringBuffer buffer = new StringBuffer();

      for (int i = 0; i < data.length; i += 3) {
         // int, not byte: as a byte the index overflowed past 127 and crashed
         // (or looped) for inputs longer than 127 bytes, e.g. long HTTP credentials.
         buffer.append(encodeBlock(data, i));
      }

      return buffer.toString();
   }

   protected static char[] encodeBlock(byte[] data, int offset) {
      int bits = 0;
      int remaining = data.length - offset - 1;
      int lastIndex = remaining >= 2 ? 2 : remaining;

      for (int i = 0; i <= lastIndex; i++) {
         byte b = data[offset + i];
         int value = b < 0 ? b + 256 : b;
         bits += value << 8 * (2 - i);
      }

      char[] chars = new char[4];

      for (int j = 0; j < 4; j++) {
         int index = bits >>> 6 * (3 - j) & 63;
         chars[j] = getChar(index);
      }

      if (remaining < 1) {
         chars[2] = '=';
      }

      if (remaining < 2) {
         chars[3] = '=';
      }

      return chars;
   }

   protected static char getChar(int value) {
      if (value >= 0 && value <= 25) {
         return (char)(65 + value);
      } else if (value >= 26 && value <= 51) {
         return (char)(97 + (value - 26));
      } else if (value >= 52 && value <= 61) {
         return (char)(48 + (value - 52));
      } else if (value == 62) {
         return '+';
      } else {
         return (char)(value == 63 ? '/' : '?');
      }
   }

   public static byte[] decode(String text) {
      int padding = 0;

      for (int i = text.length() - 1; text.charAt(i) == '='; i--) {
         padding++;
      }

      int length = text.length() * 6 / 8 - padding;
      byte[] data = new byte[length];
      int outIndex = 0;

      for (int i = 0; i < text.length(); i += 4) {
         int bits = (getValue(text.charAt(i)) << 18)
            + (getValue(text.charAt(i + 1)) << 12)
            + (getValue(text.charAt(i + 2)) << 6)
            + getValue(text.charAt(i + 3));

         for (int j = 0; j < 3 && outIndex + j < data.length; j++) {
            data[outIndex + j] = (byte)(bits >> 8 * (2 - j) & 0xFF);
         }

         outIndex += 3;
      }

      return data;
   }

   protected static int getValue(char c) {
      if (c >= 'A' && c <= 'Z') {
         return c - 65;
      } else if (c >= 'a' && c <= 'z') {
         return c - 97 + 26;
      } else if (c >= '0' && c <= '9') {
         return c - 48 + 52;
      } else if (c == '+') {
         return 62;
      } else if (c == '/') {
         return 63;
      } else {
         return c == 61 ? 0 : -1;
      }
   }
}
