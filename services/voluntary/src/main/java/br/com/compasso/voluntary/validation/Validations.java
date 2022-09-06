package br.com.compasso.voluntary.validation;

public class Validations {

        public static boolean validateZipCode(String zipCode) {

            if(zipCode.matches("^\\d{8}+$")){
                return false;
            }
            return true;
        }
}
