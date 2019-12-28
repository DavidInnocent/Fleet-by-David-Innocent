package ke.co.ximmoz.fleet.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerClusters implements ClusterItem {

    private final LatLng positiond;
    private final String markerId;
    private final String title;



    private final String snippet;

    public MarkerClusters(LatLng positiond, String markerId, String title, String snippet) {
        this.positiond = positiond;
        this.markerId = markerId;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return positiond;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public String getMarkerId() {
        return markerId;
    }
}
