package net.marcarni.easycheck;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.marcarni.easycheck.SQLite.ContracteBD;
import net.marcarni.easycheck.SQLite.DBInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BDInterfaceTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private DBInterface db;

    @Before
    public void setUp(){
        db = new DBInterface(mContext);
        db.obre();
        db.Esborra();
        // nom - cognom1 - cognom2 - login - esAdmin
        db.InserirTreballador("Pep","Barrau","Roig","sanfumut","1");

        // descripcio  - idTreballador - dataServei - horaInici - horaFi
        db.InserirServei("Tarragona - Reus", "1","29/10/2017","10:00","11:00");

        // localitzador, dataReserva, idServei, nomTitular, cognom1Titular, cognom2Titular, telefonTitular, emailTitular, qrCode, checkIn, dniTitular
        db.InserirReserva("123456","16/1/2017",1,"Maria","Ortega","Cobos","12345678","maria@gmail.com","45R545WE45","0","41471860P");
        db.InserirReserva("123446","16/3/2017",1,"Joana","Fidel","Sanchis","12345998","jaoan@gmail.com","45R545WE45","0","38039532Q");
        db.tanca();
    }

    @Test
    public void testRetornaTotesLesReserves(){
        db.obre();
        assertTrue(db.RetornaTotesLesReserves().getCount()==2);
        db.tanca();
    }

    @Test
    public void testRetornaReservaDNI_DATA(){
        db.obre();
        assertTrue(db.RetornaReservaDNI_DATA("41471860P","29/10/2017").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaReservaQR(){
        db.obre();
        assertTrue(db.RetornaReservaQR("45R545WE45").getCount()==2);
        db.tanca();
    }


    @Test
    public void testRetornaReservaLocalitzador(){
        db.obre();
        assertTrue(db.RetornaReservaLocalitzador("123456").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaReservaDNI(){
        db.obre();
        assertTrue(db.RetornaReservaDNI("41471860P").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaReservaData(){
        db.obre();
        assertTrue(db.RetornaReservaData("29/10/2017").getCount()==2);
        db.tanca();
    }

    @Test
    public void testRetornaReservaId_Servei(){
        db.obre();
        assertTrue(db.RetornaReservaId_Servei("1").getCount()==2);
        db.tanca();
    }

    @Test
    public void testRetornaTotsElsTreballadors(){
        db.obre();
        assertTrue(db.RetornaTotsElsTreballadors().getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaTotsElsServeis(){
        db.obre();
        assertTrue(db.RetornaTotsElsServeis().getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaServei_Treballador(){
        db.obre();
        assertTrue(db.RetornaServei_Treballador(1).getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaServei_Treballador_data(){
        db.obre();
        assertTrue(db.RetornaServei_Treballador_data(1,"29/10/2017").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaServei_Treballador_data_hora(){
        db.obre();
        assertTrue(db.RetornaServei_Treballador_data_hora(1,"29/10/2017","10:00").getCount()==1);
        db.tanca();
    }

    @Test
    public void testRetornaServei_data(){
        db.obre();
        assertTrue(db.RetornaServei_data("29/10/2017").getCount()==1);
        db.tanca();
    }

    @Test
    public void testActalitzaCheckInReserva(){
        db.obre();
        db.ActalitzaCheckInReserva(1);
        Cursor cursor = db.RetornaTotesLesReserves();
        cursor.moveToPosition(1);
        String checkIn = cursor.getString(cursor.getColumnIndex(ContracteBD.Reserves.CHECK_IN));
        assertTrue(checkIn.equals("1"));
        db.tanca();
    }

}
