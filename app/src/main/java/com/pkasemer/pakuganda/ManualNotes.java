package com.pkasemer.pakuganda;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Arrays;
import java.util.Collections;

public class ManualNotes extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "com.pkasemer.pakuganda.notes";
    Button newNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_notes);
        File notesDirectory = new File(getFilesDir(), "notes"); // Create a subdirectory named "notes"
        newNote = (Button) findViewById(R.id.newNote);
        notesDirectory.mkdir(); // Make the subdirectory if it doesn't exist
        String[] array = notesDirectory.list();
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        // Sort the array in reverse order
        Arrays.sort(array, Collections.reverseOrder());
        for (String filename : array) {
            filename = filename.replace(".txt", "");
            System.out.println(filename);
            adapter.add(filename);
        }
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), NoteDetail.class);
                intent.putExtra(EXTRA_MESSAGE, item);
                startActivity(intent);
            }
        });

        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TakeNote.class);
                startActivity(intent);
            }
        });
    }
}
