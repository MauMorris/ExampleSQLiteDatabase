package com.example.mauriciogodinez.basedatos1;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper myDb;

    protected EditText nameEditText;
    protected EditText lastNameEditText;
    protected EditText marksEditText;
    protected EditText idEditText;

    protected Button addDataButton;
    protected Button showDataButton;
    protected Button updateDataButton;
    protected Button deleteDataButton;

    boolean isInserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.editText_name);
        lastNameEditText = findViewById(R.id.editText_lastName);
        marksEditText = findViewById(R.id.editText_marks);
        idEditText = findViewById(R.id.editText_id);

        addDataButton = findViewById(R.id.button_addData);
        showDataButton = findViewById(R.id.button_showData);
        updateDataButton = findViewById(R.id.button_upDateData);
        deleteDataButton = findViewById(R.id.button_deleteData);

        addDataButton.setOnClickListener(this);
        showDataButton.setOnClickListener(this);
        updateDataButton.setOnClickListener(this);
        deleteDataButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int itemId = view.getId();

        switch (itemId){
            case R.id.button_addData:
                addData();
                break;
            case R.id.button_showData:
                showData();
                break;
            case R.id.button_upDateData:
                upDateData();
                break;
            case R.id.button_deleteData:
                deleteData();
                break;
            default:
                break;
        }
    }

    public void addData() {
        isInserted = myDb.insertData(nameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                marksEditText.getText().toString());
        if (isInserted)
            Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Data no inserted", Toast.LENGTH_SHORT).show();

        nameEditText.setText("");
        lastNameEditText.setText("");
        marksEditText.setText("");
    }

    public void showData() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error", "Nothing found");
        } else {
            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                buffer.append("Id :").append(res.getString(0)).append("\n");
                buffer.append("Name :").append(res.getString(1)).append("\n");
                buffer.append("LastName :").append(res.getString(2)).append("\n");
                buffer.append("Marks :").append(res.getString(3)).append("\n\n");
            }

            showMessage("Data", buffer.toString());
        }
    }

    public void upDateData() {
        boolean isUpdate = myDb.upDateDataNow(idEditText.getText().toString(),
                nameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                marksEditText.getText().toString());
        if (isUpdate)
            Toast.makeText(MainActivity.this, "Data upDated", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Data no upDated", Toast.LENGTH_SHORT).show();

        nameEditText.setText("");
        lastNameEditText.setText("");
        marksEditText.setText("");
        idEditText.setText("");
    }

    public void deleteData() {
        Integer deletedRows = myDb.deleteDataNow(idEditText.getText().toString());
        if (deletedRows > 0)
            Toast.makeText(MainActivity.this, "Data deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Data no deleted", Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}