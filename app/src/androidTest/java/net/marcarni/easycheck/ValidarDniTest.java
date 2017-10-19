package net.marcarni.easycheck;

import android.support.test.runner.AndroidJUnit4;

import net.marcarni.easycheck.eines.ValidaDNI;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * @author  Carlos Alberto Castro Cañabate
 * Comprovació del funcionament per validar numeració correcta del DNI
 */
@RunWith(AndroidJUnit4.class)
public class ValidarDniTest {

    ValidaDNI v=new ValidaDNI();
    String dni="38039532Q";
    String dni_fals="H00000056";

    /**
     * Comprovem que el dni és un dni real vàlid
     */
    @Test
    public void comprovarDniValid(){
        boolean valida=v.validarDni(dni);
        Assert.assertEquals(true,valida);
    }

    /**
     * Comprovem que dni_fals no és un dni real vàlid
     */
    @Test
    public void comprovarDniValid_false(){
        boolean valida=v.validarDni(dni_fals);
        Assert.assertEquals(false,valida);
    }





}
