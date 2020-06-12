package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class VolleyHelper {

    public void loadJsonObject(String url, String keyTrack, int action, JSONObject jsonRequest, final Map<String, String> params, Response.Listener<JSONObject> volleyCallbackResponse, Response.ErrorListener volleyErrorResponse) {


        JsonObjectRequest request = new JsonObjectRequest(action, url, jsonRequest, volleyCallbackResponse, volleyErrorResponse) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null)
                    return params;
                return super.getParams();
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationController.getInstance().addToRequestQueue(request, keyTrack + Calendar.getInstance().getTimeInMillis());
    }


    public VolleyResponse<JSONObject> loadFutureJsonObject(String url, String keyTrack, int action, JSONObject jsonRequest, final Map<String, String> params) {

        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        JsonObjectRequest request = new JsonObjectRequest(action, url, jsonRequest, future, future) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null)
                    return params;
                return super.getParams();
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(600000, 15, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationController.getInstance().addToRequestQueue(request, keyTrack + Calendar.getInstance().getTimeInMillis());

        try {
            JSONObject response = future.get(2, TimeUnit.MINUTES);
            return new VolleyResponse<>(true, response);
        } catch (InterruptedException e) {
            // exception handling
            return buildErrorMessage(e.getMessage());
        } catch (ExecutionException e) {
            // exception handling
            return buildErrorMessage(e.getMessage());
        } catch (TimeoutException e) {
            // exception handling
            return buildErrorMessage(e.getMessage());
        }

    }

    public VolleyHelper.VolleyResponse<JSONArray> loadFutureJsonArray(String url, String keyTrack, int action, JSONArray jsonRequest, final Map<String, String> params) {

        RequestFuture<JSONArray> future = RequestFuture.newFuture();

        JsonArrayRequest request = new JsonArrayRequest(action, url, jsonRequest, future, future) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null)
                    return params;
                return super.getParams();
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationController.getInstance().addToRequestQueue(request, keyTrack + Calendar.getInstance().getTimeInMillis());
        try {
            JSONArray response = future.get(2, TimeUnit.MINUTES);
            return new VolleyHelper.VolleyResponse<JSONArray>(true, response);
        } catch (Exception e) {
            return buildErrorMessage(e.getMessage());
        }
    }

    public VolleyHelper.VolleyResponse<String> loadFutureStringObject(String url, String keyTrack, int action, final Map<String, String> params) {
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(action, url, future, future) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null)
                    return params;
                return super.getParams();
            }

            @Override
            public String getBodyContentType() {
                return body.trim().isEmpty() ?
                        super.getBodyContentType() : "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (!body.trim().isEmpty())
                    return body.getBytes();
                return "".getBytes();
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationController.getInstance().addToRequestQueue(request, keyTrack + Calendar.getInstance().getTimeInMillis());


        String response = null;
        try {
            response = future.get(2, TimeUnit.MINUTES);
            if (!response.equals(""))
                return new VolleyHelper.VolleyResponse<>(true, response);
            else
                return buildErrorMessage("");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return buildErrorMessage(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return buildErrorMessage(e.getMessage());
        } catch (TimeoutException e) {
            e.printStackTrace();
            return buildErrorMessage(e.getMessage());
        }


    }

    public void loadStringObject(String url, String keyTrack, int action, final Map<String, String> params, final Response.Listener<String> volleyCallbackResponse, Response.ErrorListener volleyErrorResponse) {

        StringRequest request = new StringRequest(action, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallbackResponse.onResponse(response);
            }
        }, volleyErrorResponse) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null)
                    return params;
                return super.getParams();
            }

            @Override
            public String getBodyContentType() {
                return body.trim().isEmpty() ?
                        super.getBodyContentType() : "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (!body.trim().isEmpty())
                    return body.getBytes();
                return super.getBody();
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationController.getInstance().addToRequestQueue(request, keyTrack + Calendar.getInstance().getTimeInMillis());
    }

    String body = "";

    public void loadStringObject(String url, String keyTrack, int action, String body, final Map<String, String> params, Response.Listener<String> volleyCallbackResponse, Response.ErrorListener volleyErrorResponse) {
        this.body = body;
        loadStringObject(url, keyTrack, action, params, volleyCallbackResponse, volleyErrorResponse);
    }

    public VolleyHelper.VolleyResponse<String> loadFutureStringObject(String url, String keyTrack, int action, String body, final Map<String, String> params) {
        this.body = body;
        return loadFutureStringObject(url, keyTrack, action, params);
    }


    public interface VolleyResponseInterface<T> {
        void onResponse(T t);

        void onGotoError(JSONObject error);

        void onError(String error);
    }

    public VolleyResponse buildErrorMessage(String errorMessage) {
        VolleyResponse volleyResponse = new VolleyResponse(false, null);
        volleyResponse.setErrorMessage(errorMessage);
        return volleyResponse;
    }

    public static class VolleyResponse<T> {
        private boolean isSuccess = false;
        private T response;
        private String errorMessage;

        public String getErrorMessage() {
            return this.errorMessage;
        }

        public VolleyHelper.VolleyResponse setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public VolleyResponse(boolean isSuccess, T response) {
            this.isSuccess = isSuccess;
            this.response = response;
        }

        public boolean isSuccess() {
            return this.isSuccess;
        }

        public VolleyHelper.VolleyResponse setSuccess(boolean success) {
            this.isSuccess = success;
            return this;
        }

        public T getResponse() {
            return this.response;
        }

        public VolleyHelper.VolleyResponse setResponse(T response) {
            this.response = response;
            return this;
        }
    }


}
