package sancho.model.mldonkey.utility;

import java.util.ArrayList;
import java.util.List;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumQuery;

public class Query {
   private ICore core;
   private EnumQuery enumQuery;
   private Query query1;
   private Query query2;
   private List queryList = new ArrayList();
   private String string1;
   private String string2;

   public Query(ICore core) {
      this.core = core;
   }

   public void addQuery(Query query) {
      this.queryList.add(query);
   }

   public int queryListSize() {
      return this.queryList.size();
   }

   public void addQueryToList(List list, Query query) {
      Object[] objects = query.toObjectArray();

      for (int i = 0; i < objects.length; i++) {
         list.add(objects[i]);
      }
   }

   public Query createQuery() {
      return UtilityFactory.getQuery(this.core);
   }

   public EnumQuery getEnumQuery() {
      return this.enumQuery;
   }

   public Query[] getQueryList() {
      Query[] queries = new Query[this.queryList.size()];
      this.queryList.toArray(queries);
      return queries;
   }

   public void read(MessageBuffer buffer) {
      this.enumQuery = EnumQuery.byteToEnum(buffer.getByte());
      if (this.enumQuery != EnumQuery.AND && this.enumQuery != EnumQuery.OR && this.enumQuery != EnumQuery.HIDDEN) {
         if (this.enumQuery == EnumQuery.ANDNOT) {
            this.query1 = this.createQuery();
            this.query1.read(buffer);
            this.query2 = this.createQuery();
            this.query2.read(buffer);
         } else if (this.enumQuery == EnumQuery.MODULE) {
            this.string1 = buffer.getString();
            this.query1 = this.createQuery();
            this.query1.read(buffer);
         } else {
            this.string1 = buffer.getString();
            this.string2 = buffer.getString();
         }
      } else {
         int count = buffer.getUInt16();

         for (int i = 0; i < count; i++) {
            Query query = this.createQuery();
            query.read(buffer);
            this.queryList.add(query);
         }
      }
   }

   public void setEnumQuery(EnumQuery enumQuery) {
      this.enumQuery = enumQuery;
   }

   public void setQuery1(Query query) {
      this.query1 = query;
   }

   public void setQuery2(Query query) {
      this.query2 = query;
   }

   public void setString1(String text) {
      this.string1 = text;
   }

   public void setString2(String text) {
      this.string2 = text;
   }

   public Object[] toObjectArray() {
      ArrayList list = new ArrayList();
      list.add(Byte.valueOf(this.enumQuery.getByteValue()));
      if (this.enumQuery != EnumQuery.AND && this.enumQuery != EnumQuery.OR && this.enumQuery != EnumQuery.HIDDEN) {
         if (this.enumQuery == EnumQuery.ANDNOT) {
            this.addQueryToList(list, this.query1);
            this.addQueryToList(list, this.query2);
         } else if (this.enumQuery == EnumQuery.MODULE) {
            list.add(this.string1);
            this.addQueryToList(list, this.query1);
         } else {
            list.add(this.string1);
            list.add(this.string2);
         }
      } else {
         list.add(Short.valueOf((short)this.queryList.size()));

         for (int i = 0; i < this.queryList.size(); i++) {
            this.addQueryToList(list, (Query)this.queryList.get(i));
         }
      }

      return list.toArray();
   }
}
