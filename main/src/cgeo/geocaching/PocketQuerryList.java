package cgeo.geocaching;

import cgeo.geocaching.connector.gc.GCParser;
import cgeo.geocaching.utils.RunnableWithArgument;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import java.util.List;

public final class PocketQuerryList {

    private final String guid;
    private final int maxCaches;
    private final String name;

    public PocketQuerryList(String guid, String name, int maxCaches) {
        this.guid = guid;
        this.name = name;
        this.maxCaches = maxCaches;
    }

    public static class UserInterface {

        List<PocketQuerryList> pocketQuerryList = null;
        RunnableWithArgument<PocketQuerryList> runAfterwards;

        private Handler loadPocketQuerryHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if ((pocketQuerryList == null) || (pocketQuerryList.size() == 0)) {
                    if (waitDialog != null) {
                        waitDialog.dismiss();
                    }

                    //showToast("No pocket querry stored!"); //TODO

                    return;
                }

                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

                final CharSequence[] items = new CharSequence[pocketQuerryList.size()];

                for (int i = 0; i < pocketQuerryList.size(); i++) {
                    PocketQuerryList pq = pocketQuerryList.get(i);

                    items[i] = pq.name + "(" + pq.maxCaches + ")";

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Choose Pocket Querry"); //TODO
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int itemId) {
                        runAfterwards.run(pocketQuerryList.get(itemId));
                    }
                });
                builder.create().show();

            }
        };

        private class LoadPocketQuerryListThread extends Thread {
            final private Handler handler;

            public LoadPocketQuerryListThread(Handler handlerIn) {
                handler = handlerIn;
            }

            @Override
            public void run() {
                pocketQuerryList = GCParser.searchPocketQuerryList();
                handler.sendMessage(Message.obtain());
            }
        }

        private final Activity activity;
        private final cgeoapplication app;
        private final Resources res;
        private ProgressDialog waitDialog = null;

        public UserInterface(final Activity activity) {
            this.activity = activity;
            app = cgeoapplication.getInstance();
            res = app.getResources();
        }

        public void promptForListSelection(final RunnableWithArgument<PocketQuerryList> runAfterwards) {

            this.runAfterwards = runAfterwards;

            waitDialog = ProgressDialog.show(activity, "Pocket querry", "Loading a list of PQs", true, true); //TODO

            LoadPocketQuerryListThread thread = new LoadPocketQuerryListThread(loadPocketQuerryHandler);
            thread.start();
        }


    }

    public String getGuid() {
        return guid;
    }

    public int getMaxCaches() {
        return maxCaches;
    }

    public String getName() {
        return name;
    }

}
