package com.omneagate.lip.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.lip.R;


/**
 * Created by user1 on 18/1/16.
 */
public class DialogSelect extends Dialog implements
        View.OnClickListener {


    private final Activity context;  //    Context from the user
    private int imagePostion;

    /*Constructor class for this dialog*/
    public DialogSelect(Activity _context, int imagePostion_) {
        super(_context);
        context = _context;
        imagePostion = imagePostion_;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.grievane_notification);
        setCancelable(false);
        Button okButton = (Button) findViewById(R.id.buttonNwOk);
        okButton.setOnClickListener(this);
        Button cancelButton = (Button) findViewById(R.id.buttonNwCancel);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNwOk:
                dismiss();
                ((GrievancesActivity) context).captureImage(imagePostion);
                break;
            case R.id.buttonNwCancel:
                ((GrievancesActivity) context).previewImage(imagePostion);
                dismiss();
                break;
        }
    }


}
