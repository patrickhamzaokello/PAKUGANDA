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
        File notesDirectory = new File(getFilesDir(), "notes"); // Create a subdirectory named "notes"
        notesDirectory.mkdir(); // Make the subdirectory if it doesn't exist
        String[] array = notesDirectory.list();
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        for (String filename : array) {
            filename = filename.replace(".txt", "");
            System.out.println(filename);
            adapter.add(filename);
        }
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
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
                        adapter.add(heading);
                        listView.setAdapter(adapter);
                        Toast.makeText(TakeNote.this, "Note Saved", Toast.LENGTH_SHORT).show();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), NoteDetail.class);
                intent.putExtra(EXTRA_MESSAGE, item);
                startActivity(intent);
            }
        });
    }


}