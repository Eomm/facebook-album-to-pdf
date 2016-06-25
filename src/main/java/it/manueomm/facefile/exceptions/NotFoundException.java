package it.manueomm.facefile.exceptions;

public class NotFoundException extends Exception {

   public NotFoundException(String message) {
      super(message);
   }

   public NotFoundException(Exception ex) {
      super(ex);
   }

}
