package com.chiragaggarwal.bolt.run.persistance;

import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

import com.chiragaggarwal.bolt.common.OnSuccessCallback;
import com.chiragaggarwal.bolt.run.Run;

import java.util.List;

import static com.chiragaggarwal.bolt.run.persistance.BoltDatabaseSchema.RunSchema;
import static com.chiragaggarwal.bolt.run.persistance.BoltDatabaseSchema.UserLocationsSchema;

public class RunLocalStorage {
    private Context context;

    public RunLocalStorage(Context context) {
        this.context = context;
    }

    public boolean insertRun(Run run) {
        Uri resultUri = context.getContentResolver().insert(RunSchema.ALL_RUNS_RESOURCE_URI, run.persistable());
        String runRowNumber = resultUri.getLastPathSegment();
        if (runRowNumber.equals(Dao.INVALID_PATH_ROW)) {
            return false;
        }
        Long runRowNumberValue = new Long(runRowNumber);
        context.getContentResolver().bulkInsert(UserLocationsSchema.ALL_USER_LOCATIONS_URI, run.persistableUserLocations(runRowNumberValue.longValue()));
        return true;
    }

    public void loadRuns(LoaderManager loaderManager, OnSuccessCallback<List<Run>> onSuccessCallback) {
        new RunsLoader(context, loaderManager, onSuccessCallback).load();
    }

}