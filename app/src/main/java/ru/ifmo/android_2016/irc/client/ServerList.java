package ru.ifmo.android_2016.irc.client;

import android.os.AsyncTask;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import ru.ifmo.android_2016.irc.utils.FileUtils;

/**
 * Created by ghost3432 on 01.11.16.
 */

public final class ServerList extends HashMap<Long, ClientSettings> {
    private static final String TAG = ServerList.class.getSimpleName();
    private static ServerList instance = null;
    private long lastId = 1;

    ClientSettings find(long id) {
        if (id != 0) {
            ClientSettings clientSettings = get(id);
            if (clientSettings != null) {
                return clientSettings;
            }
        }
        Log.i(TAG, "Can't find setting with id " + id);
        return null;
    }

    private ServerList() {
    }

    public static ServerList getInstance() {
        return instance;
    }

    @WorkerThread
    static ServerList loadFromFile(String filename) {
        if (instance == null) {
            Log.i(TAG, "Loading " + filename);
            instance = FileUtils.readObject(filename);
            instance = instance == null ? new ServerList() : instance;
        }
        return instance;
    }

    static class SaveToFile extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            if (instance != null) {
                Log.i(TAG, "Saving to " + strings[0]);
                FileUtils.writeObject(strings[0], instance);
            }
            return null;
        }
    }

    public long add(ClientSettings clientSettings) {
        if (clientSettings.id == 0) {
            put(lastId, clientSettings.setId(lastId));
            return lastId++;
        } else {
            put(clientSettings.id, clientSettings);
            return clientSettings.id;
        }
    }

    @Override
    public void clear() {
        lastId = 1;
        super.clear();
    }
}