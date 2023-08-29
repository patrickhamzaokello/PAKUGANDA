package com.pkasemer.pakuganda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        final Intent intent = getIntent();
        final String heading = intent.getStringExtra(NoteDetail.HEADER_MSG);

        String fileName = heading + ".txt"; // Assuming 'message' is your note's heading
        String filePath = new File(getFilesDir(), "notes") + File.separator + fileName;

        Log.e("note_file", "onClick: "+ filePath );
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );
            String line;
            String whole = "";
            while ( (line = reader.readLine()) != null ) {
                if(whole == "") {
                    whole = whole + line;
                }else{
                    whole = whole + "\n" + line;
                }
            }
            reader.close();
            final EditText editText = (EditText) findViewById(R.id.contentfield);
            editText.setText(whole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final EditText editText = (EditText) findViewById(R.id.contentfield);
        TextView textView = (TextView) findViewById(R.id.heading_view);
        textView.setText("Title: "+heading);
        Button button_save = (Button) findViewById(R.id.savebutton);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = heading + ".txt"; // Assuming 'message' is your note's heading
                String filePath = new File(getFilesDir(), "notes") + File.separator + filename;
                if(!editText.getText().toString().trim().isEmpty()){
                    File dir = new File(filePath);
                    dir.delete();
                    Log.e("note_file", "onClick: "+ filePath );

                    try {
                        String content = editText.getText().toString().trim();
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);//heading will be the filename
                        fileOutputStream.write(content.getBytes());
                        fileOutputStream.close();

                        TextView status_view = (TextView) findViewById(R.id.status);
                        status_view.setText("SAVED!");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    editText.setError("Content can't be empty");
                }
            }
        });
        Button button = (Button) findViewById(R.id.back_home);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), TakeNote.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}