package sancho.model.mldonkey.utility;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class MessageEncoder {
   private ByteArrayOutputStream baOutputStream;
   private BufferedOutputStream bOutputStream;
   private byte[] numberBuffer;
   private byte[] header;
   private int length;
   private short opCode;
   private Socket socket;

   public MessageEncoder(Socket socket) {
      this.socket = socket;
      this.header = new byte[6];
      this.numberBuffer = new byte[8];
      this.baOutputStream = new ByteArrayOutputStream();
   }

   private void appendNumber(Number number) {
      byte length;
      if (number instanceof Short) {
         length = 2;
         this.numberBuffer = this.toBytes((Short)number, this.numberBuffer, 0);
      } else if (number instanceof Integer) {
         length = 4;
         this.numberBuffer = this.toBytes((Integer)number, this.numberBuffer, 0);
      } else {
         length = 8;
         this.numberBuffer = this.toBytes((Long)number, this.numberBuffer, 0);
      }

      this.baOutputStream.write(this.numberBuffer, 0, length);
   }

   private void appendObject(Object object) {
      if (object instanceof Byte) {
         this.baOutputStream.write((Byte)object);
      } else if (object instanceof Number) {
         this.appendNumber((Number)object);
      } else if (object instanceof String) {
         String text = (String)object;

         byte[] bytes;
         try {
            bytes = text.getBytes("UTF8");
         } catch (UnsupportedEncodingException unsupportedEncoding) {
            bytes = text.getBytes();
         }

         this.appendNumber(Short.valueOf((short)bytes.length));
         this.baOutputStream.write(bytes, 0, bytes.length);
      }
   }

   private int createContent(Object[] values) {
      for (int i = 0; i < values.length; i++) {
         if (values[i] instanceof byte[]) {
            byte[] bytes = (byte[])values[i];
            this.baOutputStream.write(bytes, 0, bytes.length);
         } else if (values[i].getClass().isArray()) {
            Object[] elements = (Object[])values[i];
            this.appendNumber(Short.valueOf((short)elements.length));

            for (int j = 0; j < elements.length; j++) {
               this.appendObject(elements[j]);
            }
         } else {
            this.appendObject(values[i]);
         }
      }

      return this.baOutputStream.size();
   }

   private void createHeader() {
      this.header = this.toBytes(Integer.valueOf(this.length), this.header, 0);
      this.header = this.toBytes(Short.valueOf(this.opCode), this.header, 4);
   }

   public void send(short opCode, Object[] args) throws IOException {
      this.opCode = opCode;
      this.length = 2;
      this.baOutputStream.reset();
      if (args != null) {
         this.length = this.length + this.createContent(args);
      }

      this.createHeader();
      this.write();
   }

   private byte[] toBytes(Integer value, byte[] buffer, int offset) {
      int intValue = value;
      buffer[0 + offset] = (byte)(intValue & 0xFF);
      buffer[1 + offset] = (byte)((intValue & 65535) >> 8);
      buffer[2 + offset] = (byte)((intValue & 16777215) >> 16);
      // Mask 0x7FFFFFFF cleared bit 31, so any int with the high bit set (large
      // ids, IP-as-int) had a corrupted top byte on the wire. Just shift; the
      // (byte) cast keeps the low 8 bits.
      buffer[3 + offset] = (byte)(intValue >> 24);
      return buffer;
   }

   private byte[] toBytes(Long value, byte[] buffer, int offset) {
      // Bit-shift, not the old % / / 256 division (same fix as Short/Integer above):
      // for a uint64 with bit 63 set (stored as a negative signed long) the signed
      // division truncated toward zero and only the low byte was correct.
      long longValue = value;

      for (int i = 0; i < 8; i++) {
         buffer[i + offset] = (byte)(longValue >>> i * 8);
      }

      return buffer;
   }

   private byte[] toBytes(Short value, byte[] buffer, int offset) {
      // Bit-based, not the old % / / 256 division: for a short with the high bit set
      // (a port 32768..65535 narrowed to a negative short) the signed division
      // truncated toward zero and put the wrong high byte on the wire, so the core
      // got a bogus port. Extracting the two low bytes preserves all 16 bits.
      short shortValue = value;
      buffer[offset] = (byte)(shortValue & 0xFF);
      buffer[offset + 1] = (byte)(shortValue >> 8 & 0xFF);
      return buffer;
   }

   public void write() throws IOException {
      if (this.bOutputStream == null) {
         this.bOutputStream = new BufferedOutputStream(this.socket.getOutputStream());
      }

      this.bOutputStream.write(this.header);
      if (this.length > 2) {
         this.bOutputStream.write(this.baOutputStream.toByteArray());
      }

      this.bOutputStream.flush();
   }
}
