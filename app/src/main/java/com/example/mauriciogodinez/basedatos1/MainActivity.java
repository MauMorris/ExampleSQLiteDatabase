package com.example.mauriciogodinez.basedatos1;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.mauriciogodinez.basedatos1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewHolderCallback {

    DatabaseHelper myDb;
    protected MyAdapter mAdapter;

    private ActivityMainBinding mMainBinding;
    boolean isInserted;
    private DatabaseViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mMainBinding.buttonAddData.setOnClickListener(this);
        mMainBinding.buttonShowData.setOnClickListener(this);
        mMainBinding.buttonUpDateData.setOnClickListener(this);
        mMainBinding.buttonDeleteData.setOnClickListener(this);

        mMainBinding.recyclerView.setHasFixedSize(true);
        // usa linear layout manager
        LinearLayoutManager mLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMainBinding.recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(this, this);
        mMainBinding.recyclerView.setAdapter(mAdapter);

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
                final long id = (long) viewHolder.itemView.getTag();

                DatabaseExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deleteData(String.valueOf(id));
                            }
                        });
                    }
                });
            }
        }).attachToRecyclerView(mMainBinding.recyclerView);

        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(myDb);
        mViewModel = ViewModelProviders.of(this, factory).get(DatabaseViewModel.class);

        mViewModel.getCurrentData().observe(this, new Observer<DatabaseHelper>() {
            @Override
            public void onChanged(@Nullable DatabaseHelper databaseHelper) {
                if (databaseHelper != null) {
                    mAdapter.setData(databaseHelper.getAllData());

                    mMainBinding.editTextName.getText().clear();
                    mMainBinding.editTextLastName.getText().clear();
                    mMainBinding.editTextMarks.getText().clear();
                    mMainBinding.editTextId.getText().clear();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        final int itemId = view.getId();

        DatabaseExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                                deleteData(mMainBinding.editTextId.getText().toString());
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
    }

    public void addData() {
        isInserted = myDb.insertData(mMainBinding.editTextName.getText().toString(),
                mMainBinding.editTextLastName.getText().toString(),
                mMainBinding.editTextMarks.getText().toString());

        if (isInserted){
            showToast(getString(R.string.message_alert_inserted));
            mViewModel.getCurrentData().setValue(myDb);
        } else
            showMessage(getString(R.string.title_alert_error), getString(R.string.message_alert_nothing_inserted));

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
        boolean isUpdate = myDb.upDateDataNow(mMainBinding.editTextId.getText().toString(),
                mMainBinding.editTextName.getText().toString(),
                mMainBinding.editTextLastName.getText().toString(),
                mMainBinding.editTextMarks.getText().toString());

        if (isUpdate){
            showToast(getString(R.string.message_alert_updated));
            mViewModel.getCurrentData().setValue(myDb);
        }
        else
            showMessage(getString(R.string.title_alert_error), getString(R.string.message_alert_nothing_updated));
    }

    public void deleteData(String id) {
        Integer deletedRows = myDb.deleteDataNow(id);
        if (deletedRows > 0){
            showToast(getString(R.string.message_alert_deleted));
            mViewModel.getCurrentData().setValue(myDb);
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