package it.manueomm.facefile.exceptions;

public class ConvertException extends Exception {

   public ConvertException(String message) {
      super(message);
   }

   public ConvertException(Exception ex) {
      super(ex);
   }

}
