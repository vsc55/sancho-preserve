package sancho.model.mldonkey.utility;

public class Format_OGx {
   public synchronized void read(MessageBuffer buffer) {
      int streamCount = buffer.getUInt16();

      for (int i = 0; i < streamCount; i++) {
         int streamId = buffer.getInt32();
         short streamType = buffer.getInt8();
         int tagCount = buffer.getUInt16();

         for (int j = 0; j < tagCount; j++) {
            short tagType = buffer.getInt8();
            this.readTag(tagType, buffer);
         }
      }
   }

   private void readTag(int tagType, MessageBuffer buffer) {
      switch (tagType) {
         case 0:
            String text = buffer.getString();
            break;
         case 1:
            int intValue1 = buffer.getInt32();
            break;
         case 2:
            int intValue2 = buffer.getInt32();
            break;
         case 3:
            boolean boolValue1 = true;
            break;
         case 4:
            boolean boolValue2 = true;
            break;
         case 5:
            int intValue3 = buffer.getInt32();
            break;
         case 6:
            float floatValue1 = buffer.getFloat();
            break;
         case 7:
            int intValue4 = buffer.getInt32();
            break;
         case 8:
            float floatValue2 = buffer.getFloat();
            break;
         case 9:
            float floatValue3 = buffer.getFloat();
            break;
         case 10:
            float floatValue4 = buffer.getFloat();
            break;
         case 11:
            int subTagCount = buffer.getUInt16();

            for (int k = 0; k < subTagCount; k++) {
               short subTagType = buffer.getInt8();
               switch (subTagType) {
                  case 0:
                     float subFloatValue1 = buffer.getFloat();
                     break;
                  case 1:
                     float subFloatValue2 = buffer.getFloat();
                     break;
                  case 2:
                     float subFloatValue3 = buffer.getFloat();
               }
            }
            break;
         case 12:
            int intValue5 = buffer.getInt32();
            break;
         case 13:
            int intValue6 = buffer.getInt32();
            break;
         case 14:
            float floatValue5 = buffer.getFloat();
            break;
         case 15:
            float floatValue6 = buffer.getFloat();
            break;
         case 16:
            float floatValue7 = buffer.getFloat();
            break;
         case 17:
            float floatValue8 = buffer.getFloat();
            break;
         case 18:
            short shortValue = buffer.getInt8();
            break;
         case 19:
            int intValue7 = buffer.getInt32();
            break;
         case 20:
            int intValue8 = buffer.getInt32();
      }
   }
}
