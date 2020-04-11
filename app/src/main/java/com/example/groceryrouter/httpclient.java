package com.example.groceryrouter;



import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * This example shows how to upload files using POST requests
 * with encoding type "multipart/form-data".
 * For more details please read the full tutorial
 * on https://javatutorial.net/java-file-upload-rest-service
 * @author javatutorial.net
 */
public class httpclient {
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

    public static void getResponse(String name) {
        try {
            String sourceFileUri = name;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);
            if (sourceFile.isFile()) {
                try {
                    String upLoadServerUri = "https://vehicleroutingproblem.herokuapp.com//handleUpload";

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

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"datax\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();
                    Log.d("message",serverResponseMessage);
                    Log.d("response-code",Integer.toString(serverResponseCode));
                    try {
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        StringWriter writer = new StringWriter();
                        IOUtil.copy(in, writer, "UTF-8");
                        Log.d("Resonse",writer.toString());
                    } finally {
                        conn.disconnect();
                    }

                    if (serverResponseCode == 200) {

                        // messageText.setText(msg);
                        //Toast.makeText(ctx, "File Upload Complete.",
                        //      Toast.LENGTH_SHORT).show();

                        // recursiveDelete(mDirectory1);

                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {
                    // dialog.dismiss();
                    e.printStackTrace();
                }
                // dialog.dismiss();
            } // End else block
        } catch (Exception ex) {
            // dialog.dismiss();
            ex.printStackTrace();
        }
        return ;


    }







}

