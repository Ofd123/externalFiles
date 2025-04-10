package com.example.externalfiles;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    /**
     * this is the main activity
     */
    private final String FILENAME = "exttest.txt";
    private static final int REQUEST_CODE_PERMISSION = 1;

    EditText input;
    TextView showingText;
    Button save,reset,exit;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        showingText = findViewById(R.id.textView);
        save = findViewById(R.id.saveBtn);
        reset = findViewById(R.id.resetBtn);
        exit = findViewById(R.id.exitBtn);

        if(!isExternalStorageAvailable())
        {
            requestPermission();
        }

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!dir.exists()) dir.mkdirs();

        file = new File(dir, FILENAME);
        loadFromFile();
    }

    /**
     * this method appends the text to the file
     * @param text
     */
    private void appendToFile(String text)
    {
        try (FileWriter writer = new FileWriter(file, true))
        {
            writer.append(text).append("\n");
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * this method loads the text from the file
     */
    private void loadFromFile()
    {
        StringBuilder content = new StringBuilder();
        if (file.exists())
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        showingText.setText(content.toString());
    }
    /**
     * this method clears the file
     */
    private void clearFile()
    {
        try (FileWriter writer = new FileWriter(file, false))
        {
            writer.write("");
            Toast.makeText(this, "History reset", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Error clearing file", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * this method checks if the external storage is available
     * @return
     */
    public boolean isExternalStorageAvailable()
    {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
    }
    /*
     * this method checks if the external storage is writable
     * @return
     */
    private void requestPermission()
    {
    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }
    /**
     * this method saves the text in the file
     * @param view
     */
    public void save(View view)
    {
        appendToFile(input.getText().toString());
        loadFromFile();
        input.setText("");
    }

    /**
     * this method resets the text in the file
     * @return
     */
    public void reset(View view)
    {
        clearFile();
        loadFromFile();
    }
    /**
     * this method saves the text and exits the app
     * @param view
     */
    public void exit(View view)
    {
        save(view);
        finish();
    }

    /**
     * this method checks if the permission is granted
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission to access external storage granted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * this method opens the credits activity
     * @param view
     */
    public void credits(View view)
    {
        Intent intent = new Intent(this, credits.class);
        startActivity(intent);
    }
}