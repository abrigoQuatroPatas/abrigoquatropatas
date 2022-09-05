package br.com.compasso.pet.validations;

public class Validations {

        public static boolean validateZipCode(String zipCode) {

            if(zipCode.matches("^\\d{8}+$")){
                return false;
            }
            return true;
        }
}
