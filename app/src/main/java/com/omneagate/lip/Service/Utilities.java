package com.omneagate.lip.Service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.omneagate.lip.Activity.NoInternetActivity;
import com.omneagate.lip.Utility.NetworkConnection;

import java.util.HashMap;

/**
 * Created by Sathya Rathinavelu on 5/9/2016.
 */
public class Utilities {

    public static Utilities mInstance = null;
    ProgressDialog pDialog;
    private HashMap<String, Object> inputParams;
    NetworkConnection networkConnection;
    Dialog dialog;


    public static Utilities getInstance() {
        if (mInstance == null) {
            mInstance = new Utilities();
        }
        return mInstance;
    }

  /*  public void showProgressDialog(Context ctx) {
        pDialog = new ProgressDialog(ctx);
        pDialog.setCancelable(false);
        pDialog.setMessage(ctx.getString(R.string.loading));
        pDialog.show();
    }*/

    public void showProgressBar(Context ctx) {
        dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissProgressBar() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

   /* public void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }*/

    /**
     * @param mContext     Context of the Calling activity
     * @param showProgress whether u want to show progress or not
     * @param URL          Service URL
     * @param hmParams     Post value for URL
     * @param TAG          for identify a service by unique name.
     * @param listener     Object of Listener
     */

    public void makeServiceCall(Context mContext, boolean showProgress, final String URL,
                                HashMap<String, Object> hmParams, String TAG,
                                final ServiceResponseListener listener, String url_enum,
                                String method) {
        try {
            networkConnection = new NetworkConnection(mContext);

            if (networkConnection.isNetworkAvailable()) {


                if (method.equalsIgnoreCase("GET")) {
                    showProgressBar(mContext);
                    ServiceRequest.getInstance().execute(URL, hmParams, listener, TAG, url_enum);
                } else if (method.equalsIgnoreCase("POST")) {
                    showProgressBar(mContext);
                    ServiceRequest.getInstance().execute_POST(URL, hmParams, listener, TAG, url_enum);
                }

                //Show Progress bar
                if (showProgress && mContext != null) {

                }

            } else {
                Intent i = new Intent(mContext, NoInternetActivity.class);
                mContext.startActivity(i);
                Toast.makeText(mContext, "No network connectivity", Toast.LENGTH_SHORT).show();
                dismissProgressBar();

                if (showProgress) {
                    //Show Toast for User action.
                    dismissProgressBar();
//                    BlazeUtils.getInstance().showLongToast(Application.getInstance().getApplicationContext().getResources().getString(R.string.no_connection));
                } else {
                    listener.onNetWorkFailure(URL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serviceCall_loadMore(Context mContext, boolean showProgress, final String URL,
                                HashMap<String, Object> hmParams, String TAG,
                                final ServiceResponseListener listener, String url_enum,
                                String method) {
        try {
            networkConnection = new NetworkConnection(mContext);

            if (networkConnection.isNetworkAvailable()) {


                if (method.equalsIgnoreCase("GET")) {
//                    showProgressBar(mContext);
                    ServiceRequest.getInstance().execute(URL, hmParams, listener, TAG, url_enum);
                } else if (method.equalsIgnoreCase("POST")) {
//                    showProgressBar(mContext);
                    ServiceRequest.getInstance().execute_POST(URL, hmParams, listener, TAG, url_enum);
                }

                //Show Progress bar
                if (showProgress && mContext != null) {

                }

            } else {
                Intent i = new Intent(mContext, NoInternetActivity.class);
                mContext.startActivity(i);
                Toast.makeText(mContext, "No network connectivity", Toast.LENGTH_SHORT).show();
//                dismissProgressBar();

                if (showProgress) {
                    //Show Toast for User action.
//                    dismissProgressBar();
//                    BlazeUtils.getInstance().showLongToast(Application.getInstance().getApplicationContext().getResources().getString(R.string.no_connection));
                } else {
                    listener.onNetWorkFailure(URL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void makeServiceCall_(Context mContext, boolean showProgress, final String URL, String hmParams, String TAG, final ServiceResponseListener listener, String url_enum, String method) {
        try {
            networkConnection = new NetworkConnection(mContext);

            if (networkConnection.isNetworkAvailable()) {


                if (method.equalsIgnoreCase("POST")) {
                    showProgressBar(mContext);
                    ServiceRequest.getInstance().execute_POST_(URL, hmParams, listener, TAG, url_enum);
                }

                //Show Progress bar
                if (showProgress && mContext != null) {

                }

            } else {
                Intent i = new Intent(mContext, NoInternetActivity.class);
                mContext.startActivity(i);
                Toast.makeText(mContext, "No network connectivity", Toast.LENGTH_SHORT).show();
                dismissProgressBar();

                if (showProgress) {
                    //Show Toast for User action.
                    dismissProgressBar();
//                    BlazeUtils.getInstance().showLongToast(Application.getInstance().getApplicationContext().getResources().getString(R.string.no_connection));
                } else {
                    listener.onNetWorkFailure(URL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show error message using this custom view dialog.
     *
     * @param mContext calling activity context
     * @param msgText  Message need to show
     */
    public void showErrorDialog(final Context mContext, String msgText) {

        final AlertDialog.Builder pagesDialog = new AlertDialog.Builder(mContext);
        pagesDialog.setCancelable(false);

        LayoutInflater in = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = in.inflate(R.layout.error_dialog_layout, null);
        TextView tvMsg = (TextView) dialogView.findViewById(R.id.tv_msgText);
        tvMsg.setText(msgText);
//        tvMsg.setTypeface(getSemiBoldFont());

        pagesDialog.setView(dialogView);
        final AlertDialog alert = pagesDialog.create();
        alert.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alert.dismiss();
            }
        }, 3000);
    }

    public HashMap<String, Object> getRequestParams() {
        inputParams = new HashMap<>();
        return inputParams;
    }
}
