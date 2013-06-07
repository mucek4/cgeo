package cgeo.geocaching.loaders;

import cgeo.geocaching.SearchResult;
import cgeo.geocaching.Settings;
import cgeo.geocaching.connector.gc.GCParser;

import android.content.Context;

public class PocketGeocacheListLoader extends AbstractSearchLoader {
    private final String guid;

    public PocketGeocacheListLoader(Context context, String guid) {
        super(context);
        this.guid = guid;
    }

    @Override
    public SearchResult runSearch() {

        SearchResult search = new SearchResult();
        if (Settings.isGCConnectorActive()) {
            search = GCParser.searchByPocket(guid, Settings.getCacheType(), Settings.isShowCaptcha(), this);
        }

        return search;
    }

}
