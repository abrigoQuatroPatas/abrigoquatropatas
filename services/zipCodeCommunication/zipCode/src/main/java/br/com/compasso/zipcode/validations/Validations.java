package br.com.compasso.zipcode.validations;

public class Validations {

        public static boolean validateZipCode(String zipCode) {

            if(zipCode.matches("^\\d{8}+$")){
                return false;
            }
            return true;
        }
}
