package sancho.model.mldonkey.utility;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import sancho.core.ICore;

public class MessageBuffer {
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final Tag[] EMPTY_TAG_ARRAY = new Tag[0];
   private static final int CAPACITY = 8192;
   private BufferedInputStream bInputStream;
   private byte[] byteArray;
   private ICore core;
   private byte[] intByteArray;
   private byte[] messageByteArray;
   private int lastLength;
   private int iterator;
   private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

   public MessageBuffer(ICore core, BufferedInputStream inputStream) {
      this.core = core;
      this.iterator = 0;
      this.bInputStream = inputStream;
      this.intByteArray = new byte[4];
      this.messageByteArray = new byte[8192];
   }

   public int getIterator() {
      return this.iterator;
   }

   public boolean getBool() {
      return this.getByte() == 1;
   }

   public byte getBoolOption() {
      return this.getByte();
   }

   public byte getByte() {
      return this.byteArray[this.iterator++];
   }

   public float getFloat() {
      float value = 0.0F;

      try {
         value = Float.parseFloat(this.getString());
      } catch (NumberFormatException numberFormatException) {
      }

      return value;
   }

   public int getInt32() {
      return this.getByte() & 0xFF | (this.getByte() & 0xFF) << 8 | (this.getByte() & 0xFF) << 16 | this.getByte() << 24;
   }

   public int[] getInt32List() {
      int count = this.getUInt16();
      int[] values = new int[count];

      for (int i = 0; i < count; i++) {
         values[i] = this.getInt32();
      }

      return values;
   }

   public short getInt8() {
      return (short)(this.getByte() & 255);
   }

   public void getIP(byte[] ip) {
      for (int i = 0; i < 4; i++) {
         ip[i] = this.getByte();
      }
   }

   public byte[] getMd4() {
      byte[] md4 = new byte[16];
      boolean allZero = true;

      for (int i = 0; i < 16; i++) {
         md4[i] = this.getByte();
         if (md4[i] != 0) {
            allZero = false;
         }
      }

      return allZero ? null : md4;
   }

   public int getSignedInt32() {
      int value = 0;

      for (int i = 0; i < 4; i++) {
         if (this.core.getProtocol() > 16) {
            value |= (this.getByte() & 255) << i * 8;
         } else {
            value |= this.getByte() << i * 8;
         }
      }

      return value;
   }

   public String getString() {
      return this.getString(true);
   }

   public String getString(boolean intern) {
      int length = this.getUInt16();
      if (length >= 65535) {
         length = this.getInt32();
      }

      if (length > 0 && this.iterator + length <= this.byteArray.length) {
         // Decode with an explicit charset so the result never depends on the JVM
         // default (which flipped to UTF-8 in JDK 18, so the same bytes rendered
         // differently on JDK 17 vs 18+). Modern mldonkey sends UTF-8. The bounds
         // check guards a negative/oversized length (getInt32 can be negative) that
         // would otherwise throw StringIndexOutOfBounds out of the read loop.
         String text = new String(this.byteArray, this.iterator, length, StandardCharsets.UTF_8);
         this.iterator += length;
         return text.intern();
      } else {
         return "";
      }
   }

   public String[] getStringList() {
      return this.getStringList(true);
   }

   public String[] getStringList(boolean intern) {
      int count = this.getUInt16();
      if (count <= 0) {
         return EMPTY_STRING_ARRAY;
      } else {
         String[] strings = new String[count];

         for (int i = 0; i < count; i++) {
            strings[i] = this.getString(intern);
         }

         return strings;
      }
   }

   public Tag[] getTagList() {
      int count = this.getUInt16();
      if (count <= 0) {
         return EMPTY_TAG_ARRAY;
      } else {
         Tag[] tags = new Tag[count];

         for (int i = 0; i < count; i++) {
            tags[i] = UtilityFactory.getTag(this.core);
            tags[i].read(this);
         }

         return tags;
      }
   }

   public int getUInt16() {
      int low = this.getByte() & 255;
      int high = this.getByte() & 255;
      return low + 256 * high;
   }

   public long getUInt64() {
      long value = 0L;

      for (int i = 0; i < 8; i++) {
         long b = (long)(this.getByte() & 255);
         b <<= i * 8;
         value += b;
      }

      return value;
   }

   public byte[] read(byte[] buffer, int length) throws IOException {
      this.lastLength = length;
      if (buffer == null) {
         if (length > 8192) {
            buffer = new byte[length];
         } else {
            buffer = this.messageByteArray;
         }
      }

      int bytesRead = 0;

      while (bytesRead < length) {
         try {
            int count = this.bInputStream.read(buffer, bytesRead, length - bytesRead);
            if (count <= 0) {
               throw new IOException();
            }

            bytesRead += count;
         } catch (SocketException socketException) {
            throw new IOException();
         }
      }

      return buffer;
   }

   public int readMessageLength() throws IOException {
      byte[] bytes = this.read(this.intByteArray, 4);
      return bytes[0] & 0xFF | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF) << 24;
   }

   public int readMessage() throws IOException {
      this.iterator = 0;
      this.byteArray = this.read(null, this.readMessageLength());
      return this.getUInt16();
   }

   public String getLastMessage() {
      int length = this.byteArray.length;
      byte[] bytes = new byte[length];
      // Copy all length bytes; the old length - 1 silently dropped the last byte.
      System.arraycopy(this.byteArray, 0, bytes, 0, length);
      return hexDump(bytes, this.lastLength);
   }

   public int getLastLength() {
      return this.lastLength;
   }

   public static String hexDump(byte[] bytes, int length) {
      // offset accumulator must be int: as a byte it wrapped after 4096 bytes and
      // printed wrong offset labels for large messages.
      int offset = 0;
      StringBuffer result = new StringBuffer(length);
      int i = 0;

      while (i < length) {
         String offsetLabel = "0000000" + Integer.toString(offset, 16);
         result.append(offsetLabel.substring(offsetLabel.length() - 8)).append("  ");
         StringBuffer ascii = new StringBuffer(16);
         int j = 0;

         for (j = 0; i < length && j < 16; i++) {
            result.append(byteToHex(bytes[i])).append(' ');
            char c = (char)bytes[i];
            ascii.append(c > ' ' && c < 127 ? c : '.');
            if (j == 7) {
               result.append(" ");
            }

            j++;
         }

         if (j < 8) {
            result.append(" ");
         }

         for (int k = j; k < 16; k++) {
            result.append("   ");
            ascii.append(" ");
         }

         result.append(" ").append("|").append(ascii).append("|");
         if (i > 0 && i % 16 == 0) {
            result.append("\n");
            offset += 16;
         }
      }

      return result.toString();
   }

   public static final String byteToHex(byte b) {
      char[] chars = new char[]{HEX_DIGITS[b >>> 4 & 15], HEX_DIGITS[b & 15]};
      return new String(chars);
   }
}
