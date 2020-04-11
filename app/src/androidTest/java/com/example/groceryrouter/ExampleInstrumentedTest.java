package com.example.groceryrouter;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.groceryrouter", appContext.getPackageName());
    }
    @Test
    public void getResponse(){
        String TESTSTRING = "lat_w,long_w,lat_d,long_d,cap_d,id_d,cap_v,id_v\n" +
                "11,11,20,20,20,1,30,apoorv\n" +
                "x,x,10,10,10,2,x,10\n" +
                "0,0,x,x,10,10,10,2\n";
        Log.d("test_string",TESTSTRING);
        try {
            File myfile = File.createTempFile("sample.csv",null,getApplicationContext().getCacheDir());
            FileOutputStream fOut = new FileOutputStream(myfile);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(TESTSTRING);
            osw.close();
            Log.d("file_loc",myfile.getAbsolutePath());
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(myfile));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }

                br.close();

            }
            catch (Exception e) {
                Log.d("file_content","error");
                //You'll need to add proper error handling here
            }

            Log.d("xx","xx");
            Log.d("file_content",text.toString());
            httpclient client = new httpclient();
            client.getResponse(myfile.getAbsolutePath());
        }
        catch (IOException e)
        {
            Log.d("err_test","Unable to create file");
        }

    }


}

