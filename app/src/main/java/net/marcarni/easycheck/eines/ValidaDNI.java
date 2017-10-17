package net.marcarni.easycheck.eines;

/**
 * Created by Carlos
 */
public class ValidaDNI {
    /**
     * Métode que retorna true o false si el dni pasat per parametre es vàlid
     * @param dni pasat per parametre
     * @return true o false
     */
    public boolean validarDni(String dni) {
        boolean totOK = verificarDNI(dni);
        if (totOK) {
            //     Toast.makeText(ValidaDNI.this, "No s'ah rebut cap criteri de cerca", Toast.LENGTH_LONG).show();
        } else {
            //     Toast.makeText(this, "No s'ah rebut cap criteri de cerca", Toast.LENGTH_LONG).show();
        }
        return totOK;
    }

    /**
     * Mètode encarregat de verfigicar si el dni compleix una serie de requisits
     * @param cadenaDNI dni a verificar
     * @return false si  no es compleixen els requisits
     */
    private boolean verificarDNI (String cadenaDNI) {
        boolean resultat = false;
        cadenaDNI = cadenaDNI.toUpperCase();
        resultat = comprovarLongitud(cadenaDNI);
        if (resultat == true) {
            resultat = comprovarDigitsDNI(cadenaDNI);
            if (resultat == true) {
                resultat = comprovarDniValid(cadenaDNI);
            } else {
                resultat = false;
            }
        } else {
            resultat = false;
        }
        return resultat;
    }

    /**
     * Mètode per comprovar que el dni te una Longitud correcta
     * @param cadenaDNI dni
     * @return true o false
     */
    private boolean comprovarLongitud (String cadenaDNI){
        boolean resultat;
        if (cadenaDNI.length()!=9){
            resultat=false;
        } else { // El DNI té 9 digits.
            resultat=true;
        }
        return resultat;
    }

    /**
     * Mètode per verificar que tots els numeros del dni son digits i no hi ha altres caracters (sense comptar l'ultim)
     * @param cadenaNumDNI dni
     * @return true o false
     */
    private boolean comprovarDigitsDNI(String cadenaNumDNI){ // Comprovem que els primers 8 digits siguin numèrics.
        boolean correcte = true;
        char [] arrayDigits = cadenaNumDNI.toCharArray();
        for (int i=0;i<arrayDigits.length-1;i++){
            char c=arrayDigits[i];
            if (!Character.isDigit(c)){ // SI no es un digit
                correcte=false;
            }
        }
        if (arrayDigits[8]>='A' && arrayDigits[8]<='Z'){ //Si la ultima lletra es vàlida.
            if (arrayDigits[8]!='I' && arrayDigits[8]!='O' && arrayDigits[8]!='U'){
                correcte=true;
            }
        } else {
            correcte=false;
        }
        return correcte;
    }

    /**
     * Mètode per comprovar que la lletra calculada es igual a la lletra del dni
     * @param cadenaNumDNI dni
     * @return true o false
     */
    private boolean comprovarDniValid(String cadenaNumDNI){
        char lletraCalculada = calcularLletra(cadenaNumDNI);
        boolean correcte = compararLletres(cadenaNumDNI,lletraCalculada);
        return correcte;
    }

    /**
     * Mètode per calcular la lletra del dni
     * @param cadenaNum dni
     * @return true o false
     */
    private char calcularLletra(String cadenaNum){
        cadenaNum = cadenaNum.substring(0,8);
        int numeroDNI = Integer.parseInt(cadenaNum);
        char lletraCalculada = ObtenirLletraDNI(numeroDNI);
        return lletraCalculada;
    }

    /**
     * Mètode per comparar la lletra calculada amb la lletra del dni
     * @param cadenaDNI dni
     * @param lletraCalculada lletra calculada
     * @return true o false
     */
    private boolean compararLletres(String cadenaDNI, char lletraCalculada){
        boolean resultat;
        char [] arrayCadenaNum = cadenaDNI.toCharArray();
        char lletraDNI = arrayCadenaNum[8];
        if (lletraDNI==lletraCalculada){
            resultat=true;
        }
        else {
            resultat = false;
        }
        return resultat;
    }

    /**
     * Mètode que obté la lletra del dni
     * @param numeroDNI numero dni
     * @return true o false
     */
    private char ObtenirLletraDNI (int numeroDNI){
        int lletraValor;
        char lletra;
        char[] arrayLletresDNI={'T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E','T'};
        lletraValor = numeroDNI%23;
        lletra = arrayLletresDNI[lletraValor];
        return lletra;
    }
}
