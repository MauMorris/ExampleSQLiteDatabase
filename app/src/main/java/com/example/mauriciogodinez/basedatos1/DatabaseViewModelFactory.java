package com.example.mauriciogodinez.basedatos1;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class DatabaseViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final DatabaseHelper mDatabase;

    public DatabaseViewModelFactory(DatabaseHelper database){
        mDatabase = database;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DatabaseViewModel(mDatabase);
    }
}