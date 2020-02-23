package ke.co.ximmoz.fleet.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.R;

public class MakePaymentActivity extends AppCompatActivity {


    private static final String TAG = MakePaymentActivity.class.getSimpleName();
    String authUrl;
    String shortcode;
    String callback;
    String passkey;
    String mpesapaymenturl;


    @OnClick(R.id.makePayment) void makePayment()
    {
        RequestQueue requestQueue=Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, authUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    MakePaymentNow(response.get("access_token").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MakePaymentActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>  authorizationHeader=new HashMap<>();
                authorizationHeader.put("Authorization","Basic "+getToken(getResources().getString(R.string.consumer_key),getResources().getString(R.string.consumer_secret)));
                authorizationHeader.put("Accept","application/json");
                return authorizationHeader;
            }
        };

        requestQueue.add(jsonObjectRequest);


    }




    private void MakePaymentNow(String access_token) {




        Timestamp timestamp=getTimeStamp();

        JSONObject paymentTrasactionDetails=new JSONObject();
        String password=getPassword(timestamp);
        URL url=null;
        try {
            url=new URL(callback);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            paymentTrasactionDetails.put("BusinessShortCode", 604781);
            paymentTrasactionDetails.put("Password", password);
            paymentTrasactionDetails.put("Timestamp", timestamp);
            paymentTrasactionDetails.put("TransactionType", "CustomerPayBillOnline");
            paymentTrasactionDetails.put("Amount", 200);
            paymentTrasactionDetails.put("PartyA", 254703127101L);
            paymentTrasactionDetails.put("PartyB", 604781);
            paymentTrasactionDetails.put("PhoneNumber", 254703127101L);
            paymentTrasactionDetails.put("CallbackURL", url);
            paymentTrasactionDetails.put("AccountReference", "done");
            paymentTrasactionDetails.put("TransactionDesc", "donelfklsl");



        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "MakePaymentNow: "+paymentTrasactionDetails.toString());

        RequestQueue requestQueue= Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, mpesapaymenturl, paymentTrasactionDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MakePaymentActivity.this, "received", Toast.LENGTH_LONG).show();
            }
        }, error->
                error.printStackTrace())

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>  paymentHeader=new HashMap<>();
                paymentHeader.put("Authorization","Bearer "+access_token);
                paymentHeader.put("Content-Type","application/json");
                return paymentHeader;

            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private Timestamp getTimeStamp() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYYMMDDHHmmss");
        try {
            Date date=simpleDateFormat.parse(new Date().toString());
            Timestamp timestampDate=new Timestamp(date.getTime());
            return timestampDate;
        } catch (ParseException e) {

            e.printStackTrace();
            return null;
        }

    }


    private String getToken(String consumerKey,String consumerSecret) {
        String beforeEncode=consumerKey+":"+consumerSecret;
        return Base64.encodeToString(beforeEncode.getBytes(),Base64.DEFAULT);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        ButterKnife.bind(this);

        authUrl=getResources().getString(R.string.auth_url);
        shortcode=getResources().getString(R.string.shortcode);
        callback=getResources().getString(R.string.callback);
        passkey=getResources().getString(R.string.passkey);
        mpesapaymenturl=getResources().getString(R.string.mpesa_payment);

    }



    private String getPassword(Timestamp timestamp) {

        String beforeEncode=shortcode+passkey+timestamp;
        return Base64.encodeToString(beforeEncode.getBytes(),Base64.DEFAULT);

    }


}
