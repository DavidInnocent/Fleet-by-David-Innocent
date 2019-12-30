package ke.co.ximmoz.fleet.Views.Utils;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private Context context;

    public static synchronized VolleySingleton getInstance(Context context){
        if(instance==null)
        {
            instance= new VolleySingleton(context);
        }
        return instance;
    }

    private VolleySingleton(Context context) {
        this.context=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());

        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
