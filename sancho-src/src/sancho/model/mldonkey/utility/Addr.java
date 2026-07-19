package sancho.model.mldonkey.utility;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.enums.EnumClientMode;
import sancho.view.utility.SResources;

public class Addr {
   private byte[] byteArray;
   private String ipString;

   public int compareTo(Object object) {
      if (object instanceof Addr) {
         Addr other = (Addr)object;
         if (this.hasHostName() && !other.hasHostName()) {
            return 1;
         } else if (!this.hasHostName() && other.hasHostName()) {
            return -1;
         } else if (this.hasHostName() && other.hasHostName()) {
            return this.getIpString().compareToIgnoreCase(other.getIpString());
         } else if (this.getByteAddress() == null && other.getByteAddress() == null) {
            // Both firewalled/unknown -> equal. Returning 1 here (as before) for
            // both a.compareTo(b) and b.compareTo(a) broke antisymmetry and made
            // TimSort throw "Comparison method violates its general contract".
            return 0;
         } else if (other.getByteAddress() == null) {
            return 1;
         } else {
            return this.getByteAddress() == null ? -1 : compare(this.getByteAddress(), other.getByteAddress());
         }
      } else {
         return -1;
      }
   }

   public synchronized byte[] getByteAddress() {
      return this.byteArray;
   }

   private synchronized String getIpString() {
      if (this.ipString != null) {
         return this.ipString;
      } else if (this.byteArray != null) {
         StringBuffer buffer = new StringBuffer(16);
         buffer.append(this.byteArray[0] & 255);
         buffer.append(".");
         buffer.append(this.byteArray[1] & 255);
         buffer.append(".");
         buffer.append(this.byteArray[2] & 255);
         buffer.append(".");
         buffer.append(this.byteArray[3] & 255);
         return buffer.toString().intern();
      } else {
         return "";
      }
   }

   public synchronized boolean hasHostName() {
      return this.byteArray == null && this.ipString != null;
   }

   public synchronized void read(boolean isHostName, MessageBuffer buffer) {
      if (isHostName) {
         this.ipString = buffer.getString();
         this.byteArray = null;
      } else {
         if (this.byteArray == null) {
            this.byteArray = new byte[4];
         }

         buffer.getIP(this.byteArray);
         this.ipString = null;
      }

      this.readCountryCode(buffer);
   }

   public void readCountryCode(MessageBuffer buffer) {
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getBool(), buffer);
   }

   public synchronized void setUnknown() {
      this.byteArray = null;
   }

   public synchronized boolean isBlocked() {
      return false;
   }

   public String toString() {
      return this.getByteAddress() == null && this.getIpString().equals("") ? EnumClientMode.FIREWALLED.getName() : this.getIpString();
   }

   public Image getImage() {
      return SResources.getImage("f_--");
   }

   public String getCountry() {
      return "N/A";
   }

   private static int compare(byte[] left, byte[] right) {
      if (left == right) {
         return 0;
      } else if (left == null) {
         return -1;
      } else if (right == null) {
         return 1;
      } else {
         for (int i = 0; i < left.length; i++) {
            int leftByte = left[i] & 255;
            int rightByte = right[i] & 255;
            if (leftByte != rightByte) {
               return leftByte - rightByte;
            }
         }

         return 0;
      }
   }
}
