package sancho.model.mldonkey.utility;

import java.util.ArrayList;
import java.util.List;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumQuery;
import sancho.utility.SwissArmy;

public class SearchQuery {
   public static final String S_OR = "OR";
   public static final String S_AND = "AND";
   public static final String S_ANDNOT = "ANDNOT";
   public static final String S_KEYWORD = "Keyword";
   public static final String S_AUDIO = "Audio";
   public static final String S_VIDEO = "Video";
   public static final String S_IMAGE = "Image";
   public static final String S_DOCUMENT = "Doc";
   public static final String S_PROGRAM = "Pro";
   public static final String S_ARCHIVE = "Arc";
   public static final String S_CDIMAGE = "Iso";
   public static final String S_EMULECOLLECTION = "EmuleCollection";
   private static final int OR = 1;
   private static final int AND = 2;
   private static final int ANDNOT = 3;
   private static int searchId;
   private List andNotList = new ArrayList();
   private Query andQuery;
   private Query firstQuery;
   private Query orQuery;
   private ICore core;
   private int currentType;
   private int keyWords;
   private int maxSearchResults;
   private int networkId;
   private byte searchType;

   public SearchQuery(ICore core) {
      this.core = core;
      this.andQuery = UtilityFactory.getQuery(core);
      this.andQuery.setEnumQuery(EnumQuery.AND);
      this.orQuery = UtilityFactory.getQuery(core);
      this.orQuery.setEnumQuery(EnumQuery.OR);
      this.searchType = 1;
      this.networkId = 0;
   }

   public void addKeyword(String keyword) {
      if (keyword != null) {
         Query query = UtilityFactory.getQuery(this.core);
         query.setEnumQuery(EnumQuery.KEYWORDS);
         query.setString1("Keyword");
         query.setString2(keyword);
         this.keyWords++;
         if (this.keyWords == 1) {
            this.firstQuery = query;
         } else {
            switch (this.currentType) {
               case 1:
                  if (this.firstQuery != null) {
                     this.orQuery.addQuery(this.firstQuery);
                  }

                  this.orQuery.addQuery(query);
                  this.firstQuery = null;
                  break;
               case 3:
                  this.andNotList.add(query);
                  break;
               default:
                  if (this.firstQuery != null) {
                     this.andQuery.addQuery(this.firstQuery);
                  }

                  this.andQuery.addQuery(query);
                  this.firstQuery = null;
            }
         }
      }
   }

   public void addQueryToAnd(EnumQuery enumQuery, String name, String value) {
      Query query = UtilityFactory.getQuery(this.core);
      query.setEnumQuery(enumQuery);
      query.setString1(name);
      query.setString2(value);
      this.andQuery.addQuery(query);
   }

   public int getSearchId() {
      return searchId++;
   }

   public String parseWord(String word) {
      if (word.startsWith("-")) {
         this.currentType = 3;
         return word.substring(1);
      } else if (word.startsWith("+")) {
         this.currentType = 2;
         return word.substring(1);
      } else if (word.equalsIgnoreCase("|")) {
         this.currentType = 1;
         return null;
      } else if (word.equalsIgnoreCase("AND")) {
         this.currentType = 2;
         return null;
      } else if (word.equalsIgnoreCase("OR")) {
         this.currentType = 1;
         return null;
      } else if (word.equalsIgnoreCase("ANDNOT")) {
         this.currentType = 3;
         return null;
      } else {
         return word;
      }
   }

   public void send() {
      Query query;
      if (this.firstQuery != null) {
         if (this.andQuery.queryListSize() >= 1) {
            this.andQuery.addQuery(this.firstQuery);
            query = this.andQuery;
         } else {
            query = this.firstQuery;
         }
      } else if (this.orQuery.queryListSize() >= 1 && this.andQuery.queryListSize() >= 1) {
         this.andQuery.addQuery(this.orQuery);
         query = this.andQuery;
      } else if (this.orQuery.queryListSize() >= 1) {
         query = this.orQuery;
      } else {
         query = this.andQuery;
      }

      Query rootQuery;
      if (this.andNotList.size() > 0) {
         Query andNotQuery;
         if (this.andNotList.size() > 1) {
            andNotQuery = UtilityFactory.getQuery(this.core);
            andNotQuery.setEnumQuery(EnumQuery.OR);

            for (int i = 0; i < this.andNotList.size(); i++) {
               andNotQuery.addQuery((Query)this.andNotList.get(i));
            }
         } else {
            andNotQuery = (Query)this.andNotList.get(0);
         }

         rootQuery = UtilityFactory.getQuery(this.core);
         rootQuery.setEnumQuery(EnumQuery.ANDNOT);
         rootQuery.setQuery1(query);
         rootQuery.setQuery2(andNotQuery);
      } else {
         rootQuery = query;
      }

      ArrayList list = new ArrayList();
      list.add(Integer.valueOf(searchId));
      Object[] objects = rootQuery.toObjectArray();

      for (int i = 0; i < objects.length; i++) {
         list.add(objects[i]);
      }

      list.add(Integer.valueOf(this.maxSearchResults));
      list.add(Byte.valueOf(this.searchType));
      list.add(Integer.valueOf(this.networkId));
      this.core.send((short)42, list.toArray());
   }

   public void setFormat(String format) {
      this.addQueryToAnd(EnumQuery.FORMAT, "", format);
   }

   public void setLocalSearch() {
      this.searchType = 0;
   }

   public void setMaxSearchResults(int maxSearchResults) {
      this.maxSearchResults = maxSearchResults;
   }

   public void setMaxSize(long maxSize) {
      this.addQueryToAnd(EnumQuery.MAXSIZE, "", String.valueOf(maxSize));
   }

   public void setMedia(String media) {
      this.addQueryToAnd(EnumQuery.MEDIA, "", media);
   }

   public void setMinSize(long minSize) {
      this.addQueryToAnd(EnumQuery.MINSIZE, "", String.valueOf(minSize));
   }

   public void setMp3Album(String album) {
      this.addQueryToAnd(EnumQuery.MP3_ALBUM, "", album);
   }

   public void setMp3Artist(String artist) {
      this.addQueryToAnd(EnumQuery.MP3_ARTIST, "", artist);
   }

   public void setMp3Bitrate(String bitrate) {
      this.addQueryToAnd(EnumQuery.MP3_BITRATE, "", bitrate);
   }

   public void setMp3Title(String title) {
      this.addQueryToAnd(EnumQuery.MP3_TITLE, "", title);
   }

   public void setNetwork(int networkId) {
      this.networkId = networkId;
   }

   public void setSearchString(String searchString) {
      String[] words = SwissArmy.split(searchString, ' ');
      String phrase = "";
      boolean inQuote = false;

      for (int i = 0; i < words.length; i++) {
         if (words[i].startsWith("\"") && !inQuote) {
            inQuote = true;
            if (words[i].length() > 1) {
               phrase = phrase + (phrase.equals("") ? "" : " ") + words[i].substring(1);
            }
         } else if (words[i].endsWith("\"") && inQuote) {
            if (words[i].length() > 2) {
               String word = words[i].substring(0, words[i].length() - 1);
               phrase = phrase + (phrase.equals("") ? "" : " ") + word;
               this.addKeyword(this.parseWord(phrase));
               phrase = "";
               inQuote = false;
            }
         } else if (inQuote) {
            phrase = phrase + (phrase.equals("") ? "" : " ") + words[i];
         } else {
            this.addKeyword(this.parseWord(words[i]));
         }
      }

      if (!phrase.equals("")) {
         this.addKeyword(this.parseWord(phrase));
      }
   }

   public void setSubscribeSearch() {
      this.searchType = 2;
   }
}
