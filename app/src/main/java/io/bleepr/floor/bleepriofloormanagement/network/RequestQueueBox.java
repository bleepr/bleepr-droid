package io.bleepr.floor.bleepriofloormanagement.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Matthew on 17/11/2015.
 */
public class RequestQueueBox {
    private static RequestQueueBox instance;
    private RequestQueue queue;
    private static Context appContext;

    private RequestQueueBox(Context ctx){
        appContext = ctx.getApplicationContext();
        queue = Volley.newRequestQueue(appContext);
    }

    public static synchronized RequestQueueBox getInstance(Context ctx){
        if(instance == null){
            instance = new RequestQueueBox(ctx);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req){
        queue.add(req);
    }
}
