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
import java.util.Arrays;
import java.util.Objects;

public class UploadExcelActivity extends AppCompatActivity {

    private static final String TAG = "UploadExcelActivity"; // Just for logging

    File file;
    Button upload, back;
    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;
    ArrayList<String> Coordinates;
    ListView lvInternalStorage;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_excel);

        lvInternalStorage =  findViewById(R.id.lvInternalStorage);
        upload = findViewById(R.id.upload_upload_activity);
        back = findViewById(R.id.back_upload_activity);
        Coordinates = new ArrayList<>();

        checkFilePermissions();
        lvInternalStorage.setOnItemClickListener((adapterView, view, i, l) -> {
            lastDirectory = pathHistory.get(count);
            Log.d(TAG, "lastDirectory "+lastDirectory);
            Log.d(TAG, "lastPathHistory "+adapterView.getItemAtPosition(i));
            if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                Log.d(TAG, "lvInternalStorage: Selected a file for upload "+lastDirectory);
                StringBuilder path = new StringBuilder();
                for(int j=0; j<=count; j++)
                {
                    path.append("/").append(pathHistory.get(j));
                }
                String c_max = Objects.requireNonNull(getIntent().getExtras()).getString("c_max");
                assert c_max != null;
                readExcelData(path.toString(), Integer.parseInt(c_max));
            }else{
                count++;
                pathHistory.add(count, (String) adapterView.getItemAtPosition(i));
                checkInternalStorage();
                Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
            }
        });

        back.setOnClickListener(view -> {
            if(count == 0)
            {
                Log.d(TAG, "Back: You have reached the highest level directory");
            }else{
                pathHistory.remove(count);
                count --;
                checkInternalStorage();
                Log.d(TAG, "Back: "+pathHistory.get(count));
            }
        });

        upload.setOnClickListener(view -> {
            count = 0;
            pathHistory = new ArrayList<>();
            pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
            Log.d(TAG, "Upload:"+pathHistory.get(count));
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
    private void readExcelData(String filePath, int c_max)
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
            Log.d("rowsCount", String.valueOf(rowsCount));
            for(int r=1; r<rowsCount; r++)
            {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                Log.d("cellsCount", String.valueOf(cellsCount));
                if(cellsCount!=c_max)
                {
                    toastMessage("Excel File format is not correct");
                    returnToCallingActivity(c_max);
                    return;
                }
                for(int c=0; c<cellsCount; c++)
                {
                    String value = getCellAsString(row, c, fe);
                    Log.d("value", String.valueOf(value));
                    sb.append(value).append(" ");
                }
                sb.append(":");
            }
            Log.d("Total ", sb.toString());
            parseStringBuilder(sb, c_max);
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
                StringBuilder path = new StringBuilder();
                for(int i=0; i<=count; i++)
                {
                    path.append("/").append(pathHistory.get(i));
                }
                file = new File(path.toString());
                Log.d(TAG, "checkInternalStorage: directory path: "+pathHistory.get(count));
            }
            File[] listFile = file.listFiles();

            assert listFile != null;
            int counter = 0;
            for(int i = 0; i< listFile.length; i++)
            {
                char start = listFile[i].getName().charAt(0);
                if(start != '.')
                    counter+=1;
            }
            String[] fileNameStrings = new String[counter];
            counter = 0;
            for(int i = 0; i< listFile.length; i++)
            {
                char start = listFile[i].getName().charAt(0);
                if(start != '.'){
                    fileNameStrings[counter] = listFile[i].getName();
                    counter++;
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNameStrings);
            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e)
        {
            Log.d(TAG, "checkInternalStorage: NULL POINTER EXCEPTION "+e.getMessage());
        }
    }
    private void parseStringBuilder(StringBuilder mStringBuilder, int c_max)
    {
        String[] rows = mStringBuilder.toString().split(":");
        Coordinates.addAll(Arrays.asList(rows));

        Intent intent;
        if(c_max==3) {
            DeliveryCoordinatesDB db = WelcomeActivity.deliveryCoordinatesDB;
            intent = new Intent(UploadExcelActivity.this, CoordinateInputActivity.class);
            for(int i=0; i<Coordinates.size();i++)
            {
                String[] columns = Coordinates.get(i).split(" ");
                db.addData(columns[0], columns[1], columns[2]);
            }
        }
        else {
            DeliveryAgentsDB db = WelcomeActivity.deliveryAgentsDB;
            intent = new Intent(UploadExcelActivity.this, DeliveryAgentActivity.class);
            for(int i=0; i<Coordinates.size();i++)
                db.addData(Coordinates.get(i));

        }
        toastMessage("Data Successfully Imported");
        startActivity(intent);
    }
    private void returnToCallingActivity(int c_max)
    {
        Intent intent;
        if(c_max==3)
            intent = new Intent(UploadExcelActivity.this, CoordinateInputActivity.class);
        else
            intent = new Intent(UploadExcelActivity.this, DeliveryAgentActivity.class);
        startActivity(intent);
        return;
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
