package cgeo.geocaching.connector.oc;

import cgeo.geocaching.utils.Log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

public class OCCZConnector extends OCConnector {

    private static final String GEOCODE_PREFIX = "OZ";

    public OCCZConnector() {
        super("OpenCaching.CZ", "www.opencaching.cz", true, GEOCODE_PREFIX);
    }

    @Override
    @Nullable
    public String getGeocodeFromUrl(@NonNull final String url) {
        if (!StringUtils.containsIgnoreCase(url, "opencaching.cz")) {
            return null;
        }
        final String id = StringUtils.substringAfter(url, "cacheid=");
        try {
            final String geocode = GEOCODE_PREFIX + StringUtils.leftPad(Integer.toHexString(Integer.parseInt(id)), 4, '0');
            if (canHandle(geocode)) {
                return geocode;
            }
        } catch (final NumberFormatException e) {
            Log.e("Unexpected URL for opencaching.cz " + url);
        }
        return super.getGeocodeFromUrl(url);
    }
}
