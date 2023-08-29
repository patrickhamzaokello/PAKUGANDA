package com.pkasemer.pakuganda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class NoteDetail extends AppCompatActivity {

    public static final String HEADER_MSG = "com.pkasemer.pakuganda.notes.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Intent intent = getIntent();
        final String message = intent.getStringExtra(ManualNotes.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        final TextView textView = findViewById(R.id.Heading);
        textView.setText(message);
        String fileName = message + ".txt"; // Assuming 'message' is your note's heading
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
            TextView textView1 = (TextView) findViewById(R.id.content);
            textView1.setText(whole);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath);
                Intent intent = new Intent(getApplicationContext(), NoteDetail.class);
                file.delete();
                startActivity(intent);
            }
        });
        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewcon = (TextView) findViewById(R.id.content);
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                intent.putExtra(HEADER_MSG, message);
                startActivity(intent);
            }
        });
    }
}