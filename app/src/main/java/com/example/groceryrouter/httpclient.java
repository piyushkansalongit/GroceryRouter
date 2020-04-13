package com.example.groceryrouter;



import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * This example shows how to upload files using POST requests
 * with encoding type "multipart/form-data".
 * For more details please read the full tutorial
 * on https://javatutorial.net/java-file-upload-rest-service
 * @author javatutorial.net
 */
public class httpclient extends AsyncTask<String, Void, String> {
    private Activity context;

    public httpclient(Activity myActivity)
    {
        context = myActivity;
    }
    public static String getFileContent(
            FileInputStream fis,
            String          encoding ) throws IOException
    {
        try( BufferedReader br =
                     new BufferedReader( new InputStreamReader(fis, encoding )))
        {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        }
    }


    @Override
    protected String doInBackground(String[] name) {
        try {
            String sourceFileUri = name[0];
            Log.d("file_name_in_httpclien",name[0]);
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);
            if (!sourceFile.isFile())
                Log.d("file_found","No");
            if (sourceFile.isFile()) {
                try {
                    String upLoadServerUri = "https://vehicleroutingproblem.herokuapp.com/handleUpload";

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("datax", sourceFileUri);
                    Log.d("where","line 98");

                    dos = new DataOutputStream(conn.getOutputStream());
                    Log.d("where","line 101");
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"datax\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);
                    Log.d("where","line 107");
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    Log.d("where","line 116");
                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }
                    Log.d("where","line 127");
                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);
                    Log.d("where","line 133");
                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();
                    Log.d("message",serverResponseMessage);
                    Log.d("response-code",Integer.toString(serverResponseCode));
                    Log.d("where","line 140");
                    StringWriter writer = new StringWriter();
                    try {
                        InputStream in = new BufferedInputStream(conn.getInputStream());

                        IOUtil.copy(in, writer, "UTF-8");
                        Log.d("Response",writer.toString());
                        fileInputStream.close();
                        dos.flush();
                        dos.close();
                        return writer.toString();

                    } finally {
                        conn.disconnect();
                    }



                    // close the streams //


                } catch (Exception e) {
                    // dialog.dismiss();
                    Log.d("err","here");
                    e.printStackTrace();
                }
                // dialog.dismiss();
            } // End else block
        } catch (Exception ex) {
            // dialog.dismiss();
            ex.printStackTrace();
        }


        return "An error has occured";
    }




    public void onPostExecute(String result) {

        ProgressBar progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        ((TextView)context.findViewById(R.id.outputroute)).setText(result);

    }






}

