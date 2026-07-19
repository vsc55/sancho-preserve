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

   private EnumTagType(int value, String name) {
      super(value, name);
   }

   public static EnumTagType optionByteToEnum(byte value) {
      switch (value) {
         case 1:
            return BOOL;
         case 2:
            return FILE;
         default:
            return STRING;
      }
   }

   public static EnumTagType stringToEnum(String name) {
      if (name.equalsIgnoreCase("String")) {
         return STRING;
      } else if (name.equalsIgnoreCase("Ip List")) {
         return IP_LIST;
      } else if (name.equalsIgnoreCase("Int")) {
         return INT;
      } else if (name.equalsIgnoreCase("Bool")) {
         return BOOL;
      } else if (name.equalsIgnoreCase("Ip")) {
         return IP;
      } else if (name.equalsIgnoreCase("Addr")) {
         return ADDR;
      } else if (name.equalsIgnoreCase("Integer")) {
         return INT;
      } else if (name.equalsIgnoreCase("Float")) {
         return FLOAT;
      } else if (name.equalsIgnoreCase("Md4")) {
         return MD4;
      } else if (name.equalsIgnoreCase("Sha1")) {
         return SHA1;
      } else if (name.equalsIgnoreCase("Int64")) {
         return INT64;
      } else if (name.equalsIgnoreCase("Int List")) {
         return INT_LIST;
      } else if (name.equalsIgnoreCase("String List")) {
         return STRING_LIST;
      } else {
         Sancho.pDebug("EnumTagType unknown: [" + name + "]");
         return STRING;
      }
   }
}
