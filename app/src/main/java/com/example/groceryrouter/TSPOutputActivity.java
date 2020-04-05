package com.example.groceryrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Objects;

public class TSPOutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_s_p_output);
        try {
            ArrayList<String> inputCoordinates = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("deliveryCoordinates");
            assert inputCoordinates != null;
            if(!inputCoordinates.isEmpty())
            {

            }
        }
        catch(Exception ignored){}
    }
}
