package sancho.model.mldonkey.utility;

public class Format_OGx {
   public synchronized void read(MessageBuffer var1) {
      int var2 = var1.getUInt16();

      for (int var3 = 0; var3 < var2; var3++) {
         int var4 = var1.getInt32();
         short var5 = var1.getInt8();
         int var6 = var1.getUInt16();

         for (int var7 = 0; var7 < var6; var7++) {
            short var8 = var1.getInt8();
            this.readTag(var8, var1);
         }
      }
   }

   private void readTag(int var1, MessageBuffer var2) {
      switch (var1) {
         case 0:
            String var3 = var2.getString();
            break;
         case 1:
            int var4 = var2.getInt32();
            break;
         case 2:
            int var5 = var2.getInt32();
            break;
         case 3:
            boolean var6 = true;
            break;
         case 4:
            boolean var7 = true;
            break;
         case 5:
            int var8 = var2.getInt32();
            break;
         case 6:
            float var9 = var2.getFloat();
            break;
         case 7:
            int var10 = var2.getInt32();
            break;
         case 8:
            float var11 = var2.getFloat();
            break;
         case 9:
            float var12 = var2.getFloat();
            break;
         case 10:
            float var13 = var2.getFloat();
            break;
         case 11:
            int var14 = var2.getUInt16();

            for (int var15 = 0; var15 < var14; var15++) {
               short var25 = var2.getInt8();
               switch (var25) {
                  case 0:
                     float var26 = var2.getFloat();
                     break;
                  case 1:
                     float var27 = var2.getFloat();
                     break;
                  case 2:
                     float var28 = var2.getFloat();
               }
            }
            break;
         case 12:
            int var16 = var2.getInt32();
            break;
         case 13:
            int var17 = var2.getInt32();
            break;
         case 14:
            float var18 = var2.getFloat();
            break;
         case 15:
            float var19 = var2.getFloat();
            break;
         case 16:
            float var20 = var2.getFloat();
            break;
         case 17:
            float var21 = var2.getFloat();
            break;
         case 18:
            short var22 = var2.getInt8();
            break;
         case 19:
            int var23 = var2.getInt32();
            break;
         case 20:
            int var24 = var2.getInt32();
      }
   }
}
