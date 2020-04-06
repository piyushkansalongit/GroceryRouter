package com.example.groceryrouter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class UploadExcelActivity extends AppCompatActivity {

    private static final String TAG = "UploadExcelActivity"; // Just for logging

    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;
    Button upload, back;
    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;


    ArrayList<String> deliveryCoordinates;
    ListView lvInternalStorage;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_excel);

        lvInternalStorage =  findViewById(R.id.lvInternalStorage);
        upload = findViewById(R.id.upload_upload_activity);
        back = findViewById(R.id.back_upload_activity);
        deliveryCoordinates = new ArrayList<>();

        checkFilePermissions();
        lvInternalStorage.setOnItemClickListener((adapterView, view, i, l) -> {
            lastDirectory = pathHistory.get(count);
            if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                Log.d(TAG, "lvInternalStorage: Selected a file for upload "+lastDirectory);
                readExcelData(lastDirectory);
            }else{
                count++;
                pathHistory.add(count, (String) adapterView.getItemAtPosition(i));
                checkInternalStorage();
                Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
            }
        });

        upload.setOnClickListener(view -> {
            if(count == 0)
            {

            }else{
                pathHistory.remove(count);
                count --;
                checkInternalStorage();
            }
        });

        back.setOnClickListener(view -> {
            count = 0;
            pathHistory = new ArrayList<>();
            pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
            checkInternalStorage();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE") + this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if(permissionCheck!=0)
            {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},1001);
            }
            else
            {
                Log.d(TAG, "checkBTPermissions: No need to check permissions. Android Version not supported");
            }
        }
    }
    private void readExcelData(String filePath)
    {
        Log.d(TAG, "read excel data: Reading Excel File.");

        File inputFile = new File(filePath);

        try{
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            StringBuilder sb = new StringBuilder();
            FormulaEvaluator fe = workbook.getCreationHelper().createFormulaEvaluator();
            for(int r=1; r<rowsCount; r++)
            {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                for(int c=0; c<cellsCount; c++)
                {
                    if(c>2){
                        toastMessage("ERROR: Excel File Format is Incorrect");
                        break;
                    }else{
                        String value = getCellAsString(row, c, fe);
                        sb.append(value+" ");
                    }
                }
                sb.append(":");
            }
            parseStringBuilder(sb);
        }catch(FileNotFoundException e){
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
        }catch(IOException e){
            Log.e(TAG, "readExcelData: Error Reading inputStream. " + e.getMessage());
        }
    }

    private void checkInternalStorage(){
        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if(!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)){
                toastMessage("No SD Card Found");
            }
            else{
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: "+pathHistory.get(count));
            }
            listFile = file.listFiles();

            assert listFile != null;
            FilePathStrings  = new String[listFile.length];
            FileNameStrings = new String[listFile.length];

            for(int i=0; i<listFile.length; i++)
            {
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                FileNameStrings[i] = listFile[i].getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e)
        {
            Log.d(TAG, "checkInternalStorage: NULL POINTER EXCEPTION "+e.getMessage());
        }
    }
    private void parseStringBuilder(StringBuilder mStringBuilder)
    {
        String[] rows = mStringBuilder.toString().split("");
        for(int i=0; i<rows.length; i++)
            deliveryCoordinates.add(rows[i]);

        Intent intent = new Intent(UploadExcelActivity.this, MainActivity.class);
        intent.putExtra("deliveryCoordinates", deliveryCoordinates);
        startActivity(intent);
    }
    private String getCellAsString(Row row, int c, FormulaEvaluator fe)
    {
        String value = "";
        try{
            Cell cell = row.getCell(c);
            CellValue cellValue = fe.evaluate(cell);
            switch(cellValue.getCellType()){
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    value = ""+cellValue.getNumberValue();
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:

            }
        }catch(NullPointerException ignored){
        }
        return value;
    }
    private void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }
}
