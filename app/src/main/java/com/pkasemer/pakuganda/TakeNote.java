package com.pkasemer.pakuganda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TakeNote extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.pkasemer.pakuganda.notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);
        Button button = (Button) findViewById(R.id.savebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextHeading = (EditText) findViewById(R.id.editTextTextPersonName);
                EditText editTextContent = (EditText) findViewById(R.id.contentfield);
                String heading = editTextHeading.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                if (!heading.isEmpty()) {
                    if(!content.isEmpty()) {
                        String fileName = heading + ".txt";
                        String filePath = new File(getFilesDir(), "notes") + File.separator + fileName;
                        Log.e("note_file", "onClick: "+ filePath );
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(filePath);//heading will be the filename
                            fileOutputStream.write(content.getBytes());
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(TakeNote.this, "Note Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ManualNotes.class);
                        finish();

                        startActivity(intent);
                    }else {
                        editTextContent.setError("Content can't be empty!");
                    }
                }else{
                    editTextHeading.setError("Heading can't be empty!");
                }
                editTextContent.setText("");
                editTextHeading.setText("");
            }
        });
    }


}