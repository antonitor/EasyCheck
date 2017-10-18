package net.marcarni.easycheck;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.marcarni.easycheck.SQLite.DBInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * @author Maria Remedios Ortega
 *
 * Classe per comprobar la funcionalitat amb test de les consultes a la base de dades
 * de la part de reserva
 */
@RunWith(AndroidJUnit4.class)
public class DetallActivityTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private DBInterface db;

    /**
     * Inserim un servei i una reserva per comproba el bon funcionament amb les proves
     */

    @Before
    public void setUp() {
        db = new DBInterface(mContext);
        db.obre();
        db.Esborra();
        db.InserirServei("Barcelona - París", "1","30/11/2017","10:00","21:30");
        db.InserirReserva("123446", "16/3/2017", 1, "Joana", "Fidel", "Sanchis", "12345998", "jaoan@gmail.com", "45R545WE45", "0", "38039532Q");
        db.tanca();
    }

    /**
     * Comprovem que amb l'id 2 no hi ha cap reserva
     * @throws Exception
     */
    @Test
    public void testRetornaReservaId_Reserva_False_ID() throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse( db.RetornaReservaId_Servei("2").getCount()>0);
        db.tanca();

    }

    /**
     * Comprovem que hi ha una reserva amb l'id 1
     * @throws Exception
     */
    @Test
    public void testRetornaReservaId_Reserva_True_ID() throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue( db.RetornaReservaId_Servei("1").getCount()>0);
        db.tanca();

    }

    /**
     * Comprovem que no hi ha cap dni amb la numeració introduida
     * @throws Exception
     */

    @Test
    public void testRetornaReservaDNI_False() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse( db.RetornaReservaDNI("48039532Q").getCount()>0);
        db.tanca();

    }

    /**
     * comprovem que hi ha una reserva associada al dni introduit
     * @throws Exception
     */
    @Test
    public void testRetornaReservaDNI_True() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue( db.RetornaReservaDNI("38039532Q").getCount()>0);
        db.tanca();

    }

    /**
     * Comprovem que es fals que hi hagi un dni amb la data introduida
     * @throws Exception
     */
    @Test
    public void testRetornaReservaDNI_DATA_false() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse(db.RetornaReservaDNI_DATA("38039532Q","1/3/2017").getCount()>0);
        db.tanca();
    }

    /**
     * Comprovem que existeix realment una reserva amb dni i data introduits
     * @throws Exception
     */
    @Test
    public void testRetornaReservaDNI_DATA_true() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue(db.RetornaReservaDNI_DATA("38039532Q","30/11/2017").getCount()==1);
        db.tanca();
    }

    /**
     * Comprovació de la reserva amb data introduida
     * @throws Exception
     */

    @Test
    public void testRetornaReservaDATA() throws Exception{
        db = new DBInterface(mContext);
        db.obre();

        Assert.assertTrue(db.RetornaReservaData("30/11/2017").getCount()==1);
        db.tanca();
    }

    /**
     * Comprovació de la reserva amb nombre de localització
     * @throws Exception
     */
    @Test
    public void testRetornaReservaLoc ()throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue(db.RetornaReservaLocalitzador("123446").getCount()==1);
        db.tanca();
    }

    /**
     * Comprovació de la reserva amb el codi QR
     * @throws Exception
     */
    @Test
    public void testRetornaReservaQR() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertEquals(1,db.RetornaReservaQR("45R545WE45").getCount());
        db.tanca();
    }

    /**
     * Comprovació que el localizador introduit no estigui repetit
     * @throws Exception
     */
    @Test
    public void testRetornaReservaLoc_false ()throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse(db.RetornaReservaLocalitzador("123446").getCount()==2);
        db.tanca();
    }

    /**
     * Comprovació que el codi Qr no estigui repetit
     * @throws Exception
     */

    @Test
    public void testRetornaReservaQR_false() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse(db.RetornaReservaQR("45R545WE45").getCount()==3);
        db.tanca();
    }
}