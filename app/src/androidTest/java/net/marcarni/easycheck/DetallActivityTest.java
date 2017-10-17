package net.marcarni.easycheck;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.marcarni.easycheck.SQLite.DBInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DetallActivityTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private DBInterface db;


    @Before
    public void setUp() {
        db = new DBInterface(mContext);
        db.obre();
        db.Esborra();
        db.InserirServei("Barcelona - París", "1","30/11/2017","10:00","21:30");
        db.InserirReserva("123446", "16/3/2017", 1, "Joana", "Fidel", "Sanchis", "12345998", "jaoan@gmail.com", "45R545WE45", "0", "38039532Q");
        db.tanca();
    }

    @Test
    public void testRetornaReservaId_Reserva_False_ID() throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse( db.RetornaReservaId_Servei("2").getCount()>0);
        db.tanca();

    }
    @Test
    public void testRetornaReservaId_Reserva_True_ID() throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue( db.RetornaReservaId_Servei("1").getCount()>0);
        db.tanca();

    }


    @Test
    public void testRetornaReservaDNI_False() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse( db.RetornaReservaDNI("48039532Q").getCount()>0);
        db.tanca();

    }


    @Test
    public void testRetornaReservaDNI_True() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue( db.RetornaReservaDNI("38039532Q").getCount()>0);
        db.tanca();

    }

    @Test
    public void testRetornaReservaDNI_DATA_false() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse(db.RetornaReservaDNI_DATA("38039532Q","1/3/2017").getCount()>0);
        db.tanca();
    }

    @Test
    public void testRetornaReservaDNI_DATA_true() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue(db.RetornaReservaDNI_DATA("38039532Q","30/11/2017").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaReservaDATA() throws Exception{
        db = new DBInterface(mContext);
        db.obre();

        Assert.assertTrue(db.RetornaReservaData("30/11/2017").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaReservaLoc ()throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertTrue(db.RetornaReservaLocalitzador("123446").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaReservaQR() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertEquals(1,db.RetornaReservaQR("45R545WE45").getCount());
        db.tanca();
    }
    @Test
    public void testRetornaReservaLoc_false ()throws Exception {
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse(db.RetornaReservaLocalitzador("123446").getCount()==2);
        db.tanca();
    }

    @Test
    public void testRetornaReservaQR_false() throws Exception{
        db = new DBInterface(mContext);
        db.obre();
        Assert.assertFalse(db.RetornaReservaQR("45R545WE45").getCount()==3);
        db.tanca();
    }
}