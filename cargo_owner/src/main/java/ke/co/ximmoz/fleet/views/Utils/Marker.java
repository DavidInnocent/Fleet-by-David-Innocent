package ke.co.ximmoz.fleet.views.Utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import ke.co.ximmoz.fleet.models.Consignment;

public class Marker implements ClusterItem {

    private final LatLng positiond;
    private final String markerId;
    private final String title;



    private final Consignment consignment;


    private final String snippet;

    public Marker(LatLng positiond, String markerId, String title, String snippet, Consignment consignment) {
        this.positiond = positiond;
        this.markerId = markerId;
        this.title = title;
        this.snippet = snippet;
        this.consignment=consignment;
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
    public Consignment getConsignment() {
        return consignment;
    }
}
