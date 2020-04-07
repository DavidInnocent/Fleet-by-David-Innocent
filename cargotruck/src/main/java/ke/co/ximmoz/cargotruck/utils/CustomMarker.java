package ke.co.ximmoz.cargotruck.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CustomMarker implements ClusterItem {


    private final LatLng position;
    private final String markerId;
    private final String title;



    private final String snippet;

    public CustomMarker(LatLng position, String markerId, String title, String snippet) {
        this.position = position;
        this.markerId = markerId;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
