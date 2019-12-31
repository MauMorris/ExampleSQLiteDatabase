package com.example.mauriciogodinez.basedatos1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewHolderCallback {

    DatabaseHelper myDb;

    protected EditText nameEditText;
    protected EditText lastNameEditText;
    protected EditText marksEditText;
    protected EditText idEditText;

    protected RecyclerView mResultRecyclerView;
    protected MyAdapter mAdapter;

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

        mResultRecyclerView = findViewById(R.id.recycler_view);
        mResultRecyclerView.setHasFixedSize(true);
        // usa linear layout manager
        LinearLayoutManager mLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mResultRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(this, this);
        mResultRecyclerView.setAdapter(mAdapter);

        mAdapter.setData(myDb.getAllData());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                long id = (long) viewHolder.itemView.getTag();
                deleteData(String.valueOf(id));
                mAdapter.swapCursor(myDb.getAllData());
            }
        }).attachToRecyclerView(mResultRecyclerView);
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
                deleteData(idEditText.getText().toString());
                break;
            default:
                break;
        }
    }

    public void addData() {
        isInserted = myDb.insertData(nameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                marksEditText.getText().toString());
        if (isInserted){
            showToast(getString(R.string.message_alert_inserted));
            mAdapter.setData(myDb.getAllData());
        }
        else
            showMessage(getString(R.string.title_alert_error), getString(R.string.message_alert_nothing_inserted));

        nameEditText.getText().clear();
        lastNameEditText.getText().clear();
        marksEditText.getText().clear();
    }

    public void showData() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage(getString(R.string.title_alert_error), getString(R.string.message_alert_nothing_found));
        } else {
            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                buffer.append(getString(R.string.etiqueta_id, res.getString(0))).append("\n");
                buffer.append(getString(R.string.etiqueta_name, res.getString(1))).append("\n");
                buffer.append(getString(R.string.etiqueta_lastname, res.getString(2))).append("\n");
                buffer.append(getString(R.string.etiqueta_marks, res.getString(3))).append("\n\n");
            }
            showMessage(getString(R.string.title_alert_data), buffer.toString());
        }
    }

    public void upDateData() {
        boolean isUpdate = myDb.upDateDataNow(idEditText.getText().toString(),
                nameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                marksEditText.getText().toString());

        if (isUpdate){
            showToast(getString(R.string.message_alert_updated));
            mAdapter.setData(myDb.getAllData());
        }
        else
            showMessage(getString(R.string.title_alert_error), getString(R.string.message_alert_nothing_updated));

        nameEditText.getText().clear();
        lastNameEditText.getText().clear();
        marksEditText.getText().clear();
        idEditText.getText().clear();
    }

    public void deleteData(String id) {
        Integer deletedRows = myDb.deleteDataNow(id);
        if (deletedRows > 0){
            showToast(getString(R.string.message_alert_deleted));
            mAdapter.setData(myDb.getAllData());
        }
        else
            showMessage(getString(R.string.title_alert_error), getString(R.string.message_alert_nothing_deleted));
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void showToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void viewHolderOnClick(String data, String position, String tag) {
        String message = data +
                        "\nAdapterPosition: " + position +
                        "\nidTag: " + tag;
        showToast(message);
    }
}