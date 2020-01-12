package com.example.mauriciogodinez.basedatos1;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DatabaseViewModel extends ViewModel {
    private MutableLiveData<DatabaseHelper> currentData;

    public DatabaseViewModel(DatabaseHelper database) {
        currentData = new MutableLiveData<>();
        currentData.postValue(database);
    }

    public MutableLiveData<DatabaseHelper> getCurrentData() {
        return currentData;
    }
}