package com.example.mauriciogodinez.basedatos1;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    protected EditText editName;
    protected EditText editLastName;
    protected EditText editMarks;
    protected EditText editId;

    protected Button addData;
    protected Button showData;
    protected Button upDateData;
    protected Button deleteData;

    boolean isInserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        editName = (EditText) findViewById(R.id.editText_name);
        editLastName = (EditText) findViewById(R.id.editText_lastName);
        editMarks = (EditText) findViewById(R.id.editText_marks);
        editId = (EditText) findViewById(R.id.editText_id);

        addData = (Button) findViewById(R.id.button_addData);
        showData = (Button) findViewById(R.id.button_showData);
        upDateData = (Button) findViewById(R.id.button_upDateData);
        deleteData = (Button) findViewById(R.id.button_deleteData);

    }

    public void addData(View v) {
        isInserted = myDb.insertData(editName.getText().toString(),
                editLastName.getText().toString(),
                editMarks.getText().toString());
        if (isInserted)
            Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Data no inserted", Toast.LENGTH_SHORT).show();

        editName.setText("");
        editLastName.setText("");
        editMarks.setText("");
    }

    public void showData(View v) {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error", "Nothing found");
        } else {

            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                buffer.append("Id :" + res.getString(0) + "\n");
                buffer.append("Name :" + res.getString(1) + "\n");
                buffer.append("LastName :" + res.getString(2) + "\n");
                buffer.append("Marks :" + res.getString(3) + "\n\n");
            }

            showMessage("Data", buffer.toString());
        }
    }

    public void upDateData(View v) {
        boolean isUpdate = myDb.upDateDataNow(editId.getText().toString(),
                editName.getText().toString(),
                editLastName.getText().toString(),
                editMarks.getText().toString());
        if (isUpdate)
            Toast.makeText(MainActivity.this, "Data upDated", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Data no upDated", Toast.LENGTH_SHORT).show();

        editName.setText("");
        editLastName.setText("");
        editMarks.setText("");
        editId.setText("");
    }

    public void deleteData(View v) {
        Integer deletedRows = myDb.deleteDataNow(editId.getText().toString());
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
