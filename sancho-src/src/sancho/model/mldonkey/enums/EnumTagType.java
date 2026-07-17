package sancho.model.mldonkey.enums;

import sancho.core.Sancho;

public class EnumTagType extends AbstractEnum {
   public static final EnumTagType INT = new EnumTagType(1, "Int");
   public static final EnumTagType STRING = new EnumTagType(2, "String");
   public static final EnumTagType BOOL = new EnumTagType(4, "Bool");
   public static final EnumTagType FILE = new EnumTagType(8, "File");
   public static final EnumTagType IP_LIST = new EnumTagType(16, "Ip List");
   public static final EnumTagType IP = new EnumTagType(32, "Ip");
   public static final EnumTagType ADDR = new EnumTagType(64, "Addr");
   public static final EnumTagType FLOAT = new EnumTagType(128, "Float");
   public static final EnumTagType MD4 = new EnumTagType(256, "MD4");
   public static final EnumTagType SHA1 = new EnumTagType(512, "SHA1");
   public static final EnumTagType INT64 = new EnumTagType(1024, "Int64");
   public static final EnumTagType INT_LIST = new EnumTagType(2048, "Int List");
   public static final EnumTagType STRING_LIST = new EnumTagType(4096, "String List");

   private EnumTagType(int var1, String var2) {
      super(var1, var2);
   }

   public static EnumTagType optionByteToEnum(byte var0) {
      switch (var0) {
         case 1:
            return BOOL;
         case 2:
            return FILE;
         default:
            return STRING;
      }
   }

   public static EnumTagType stringToEnum(String var0) {
      if (var0.equalsIgnoreCase("String")) {
         return STRING;
      } else if (var0.equalsIgnoreCase("Ip List")) {
         return IP_LIST;
      } else if (var0.equalsIgnoreCase("Int")) {
         return INT;
      } else if (var0.equalsIgnoreCase("Bool")) {
         return BOOL;
      } else if (var0.equalsIgnoreCase("Ip")) {
         return IP;
      } else if (var0.equalsIgnoreCase("Addr")) {
         return ADDR;
      } else if (var0.equalsIgnoreCase("Integer")) {
         return INT;
      } else if (var0.equalsIgnoreCase("Float")) {
         return FLOAT;
      } else if (var0.equalsIgnoreCase("Md4")) {
         return MD4;
      } else if (var0.equalsIgnoreCase("Sha1")) {
         return SHA1;
      } else if (var0.equalsIgnoreCase("Int64")) {
         return INT64;
      } else if (var0.equalsIgnoreCase("Int List")) {
         return INT_LIST;
      } else if (var0.equalsIgnoreCase("String List")) {
         return STRING_LIST;
      } else {
         Sancho.pDebug("EnumTagType unknown: [" + var0 + "]");
         return STRING;
      }
   }
}
