package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.CategoryAdaptor;
import com.omneagate.lip.Adaptor.GrievacnceSubCategoryAdaptor;
import com.omneagate.lip.Model.ImagesStringDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Model.SubmitGrievancesDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.GPSTracker;
import com.omneagate.lip.Utility.MySharedPreference;
import com.omneagate.lip.Utility.NoDefaultSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 27/5/16.
 */
public class GrievancesActivity extends BaseActivity
        implements Handler.Callback, View.OnClickListener {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    TextView title;
    ImageView camera_img;
    public static final int MEDIA_TYPE_IMAGE = 1;
    int image_position;
//    private LinearLayout image_one_lay, image_two_lay, image_three_lay, image_four_lay, image_five_lay;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private ImageView imageView_one, imageView_two;
    private Uri fileUri;
    List<ImagesStringDto> image;
    EditText gre_description;
    Context context;
    private Bitmap grievance_image_one, grievance_image_two, grievance_image_three, grievance_image_four, grievance_image_five;
    double latitude, longitude;
    public static Bitmap perview_image;
    private static final String IMAGE_DIRECTORY_NAME = "LIP";
    private ImageView imageView_three;
    private ImageView imageView_four;
    private ImageView imageView_five;
    private TextView gre_constituency, maxwrd;
    public static ResponseDto category;
    GPSTracker gps;
    private ResponseDto sub_category;
    private GrievacnceSubCategoryAdaptor subCategory;
    String categoryId = null, subCategory_ = null;
    private CategoryAdaptor adaptor;
    SubmitGrievancesDto submitDetails;
    private GrievacnceSubCategoryAdaptor subCategoryAdaptor;
    private NoDefaultSpinner spinner_category, spinner_sub_category;
    final ResReqController controller = new ResReqController(this);
    ResponseDto responseData;
    public static String userId;
    private String selectedLanguage;
    private static final String TAG = GrievancesActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievances);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        image = new ArrayList<>();
        setUpView();
        controller.addOutboxHandler(new Handler(this));

        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();

        if (selectedLanguage.equalsIgnoreCase("en")) {

            gre_constituency.setText(responseData.getGeneralVoterDto().getDistictName() + " / " + responseData.getGeneralVoterDto().getConstituencyName());

        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            gre_constituency.setText(responseData.getGeneralVoterDto().getDistictRegionalName() + " / " + responseData.getGeneralVoterDto().getConstituencyRegionalName());


        }

        ///gre_constituency.setText(responseData.getGeneralVoterDto().getDistictName() + " / "+responseData.getGeneralVoterDto().getConstituencyName());

        subCategory();

        gps = new GPSTracker(GrievancesActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
    }

    public void setUpView() {

        imageView_one = (ImageView) findViewById(R.id.imageView_one);
        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {


            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Grievance");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("शिकायत");

            }
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageView_two = (ImageView) findViewById(R.id.imageView_two);
        imageView_three = (ImageView) findViewById(R.id.imageView_three);
        imageView_four = (ImageView) findViewById(R.id.imageView_four);
        imageView_five = (ImageView) findViewById(R.id.imageView_five);

        gre_description = (EditText) findViewById(R.id.gre_description);

        gre_constituency = (TextView) findViewById(R.id.gre_constituency);
        maxwrd = (TextView) findViewById(R.id.maxwrd);

        spinner_category = (NoDefaultSpinner) findViewById(R.id.spinner_category);
        spinner_sub_category = (NoDefaultSpinner) findViewById(R.id.spinner_sub_category);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*image_one_lay = (LinearLayout) findViewById(R.id.image_one_lay);
        image_one_lay.setOnClickListener(this);
        image_two_lay = (LinearLayout) findViewById(R.id.image_two_lay);
        image_two_lay.setOnClickListener(this);
        image_three_lay = (LinearLayout) findViewById(R.id.image_three_lay);
        image_three_lay.setOnClickListener(this);
        image_four_lay = (LinearLayout) findViewById(R.id.image_four_lay);
        image_four_lay.setOnClickListener(this);
        image_five_lay = (LinearLayout) findViewById(R.id.image_five_lay);
        image_five_lay.setOnClickListener(this);*/


        ((LinearLayout)findViewById(R.id.image_one_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_position = 1;
                if (imageView_one.getDrawable() == null) {
                    captureImage(image_position);
                } else {
                    dialogSelect(image_position);
                }
            }
        });

        ((LinearLayout)findViewById(R.id.image_two_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_position = 2;

                if(imageView_two.getDrawable() == null){
                    captureImage(image_position);
                }else{
                    dialogSelect(image_position);
                }

            }
        });

        ((LinearLayout)findViewById(R.id.image_three_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_position = 3;

                if(imageView_three.getDrawable() == null){
                    captureImage(image_position);
                }else{
                    dialogSelect(image_position);
                }

            }
        });

        ((LinearLayout)findViewById(R.id.image_four_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_position = 4;

                if(imageView_four.getDrawable() == null){
                    captureImage(image_position);
                }else{
                    dialogSelect(image_position);
                }

            }
        });

        ((LinearLayout)findViewById(R.id.image_five_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_position = 5;

                if(imageView_five.getDrawable() == null){
                    captureImage(image_position);
                }else{
                    dialogSelect(image_position);
                }
            }
        });



        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = (category.getGrievanceCategoryList().get(position).getId());
                subSubCategory(categoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategory_ = (sub_category.getGrievanceSubCategoryList().get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((Button) findViewById(R.id.submit_complaints)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.gre_description)).getWindowToken(), 0);

                registerGrievance();
            }
        });
        ((Button) findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gre_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = gre_description.getText().toString().length();
                int target_count = 200;
                int count_ = target_count - length;
                maxwrd.setText(String.valueOf(count_) + " Words left");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ((Button) findViewById(R.id.grievance_history_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent grievance_history = new Intent(GrievancesActivity.this, GrievanceHistoryActivity.class);
                startActivity(grievance_history);
            }
        });
    }

    private void subCategory() {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            Object v_id = userId;
            controller.handleMessage(ResReqController.GET_CATEGORY, inputParams, v_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subSubCategory(Object id) {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.GET_SUB_CATEGORY, inputParams, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerGrievance() {
        try {
            if (categoryId == null) {
                Toast.makeText(context, context.getString(R.string.toast_select_category), Toast.LENGTH_SHORT).show();
            } else if (subCategory_ == null) {
                Toast.makeText(context, context.getString(R.string.toast_select_sub_category), Toast.LENGTH_SHORT).show();
            } else if (((EditText) findViewById(R.id.gre_description)).getText().toString().length() == 0) {
                Toast.makeText(context, context.getString(R.string.toast_enter_description), Toast.LENGTH_SHORT).show();
            } else if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                submitDetails = new SubmitGrievancesDto();
                submitDetails.setCategoryId(Long.parseLong(categoryId));
                submitDetails.setSubCategoryId(Long.parseLong(subCategory_));
                submitDetails.setDescription(((EditText) findViewById(R.id.gre_description)).getText().toString().trim());
                submitDetails.setLat(String.valueOf(latitude));
                submitDetails.setLon(String.valueOf(longitude));
                submitDetails.setAttachments(image);
                submitDetails.setPostedBy(Long.parseLong(userId));
                String grievancesDetal = new Gson().toJson(submitDetails);
                controller.handleMessage_(ResReqController.SUB_GRIEVANCE, grievancesDetal, null);
            } else {
                gps.showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void captureImage(int img_position) {

        image_position = img_position;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        context.getString(R.string.toast_cancelled_image), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        context.getString(R.string.toast_failed_capture_image), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    private Bitmap previewCapturedImage() {
        Bitmap image_bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            image_bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
//            image_bitmap = Bitmap.createScaledBitmap(image_bitmap, 100, 150, false);
            ImagesStringDto imagesString = new ImagesStringDto();
            if (image_position == 1) {
                imageView_one.setImageBitmap(image_bitmap);
                imagesString.setImage(encodeTobase64(image_bitmap));
                image.add(imagesString);
                grievance_image_one = image_bitmap;
                ((LinearLayout) findViewById(R.id.image_two_lay)).setVisibility(View.VISIBLE);
            } else if (image_position == 2) {
                imageView_two.setImageBitmap(image_bitmap);
                imagesString.setImage(encodeTobase64(image_bitmap));
                image.add(imagesString);
                ((LinearLayout) findViewById(R.id.image_three_lay)).setVisibility(View.VISIBLE);
                grievance_image_two = image_bitmap;
            } else if (image_position == 3) {
                imageView_three.setImageBitmap(image_bitmap);
                imagesString.setImage(encodeTobase64(image_bitmap));
                image.add(imagesString);
                ((LinearLayout) findViewById(R.id.image_four_lay)).setVisibility(View.VISIBLE);
                grievance_image_three = image_bitmap;
            } else if (image_position == 4) {
                imageView_four.setImageBitmap(image_bitmap);
                imagesString.setImage(encodeTobase64(image_bitmap));
                image.add(imagesString);
                ((LinearLayout) findViewById(R.id.image_five_lay)).setVisibility(View.VISIBLE);
                grievance_image_four = image_bitmap;
            } else if (image_position == 5) {
                imageView_five.setImageBitmap(image_bitmap);
                imagesString.setImage(encodeTobase64(image_bitmap));
                image.add(imagesString);
                grievance_image_five = image_bitmap;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return image_bitmap;
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
       /* else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } */
        else {
            return null;
        }

        return mediaFile;
    }


    public void previewImage(int postionImage) {
        if (postionImage == 1) {
            perview_image = grievance_image_one;

        } else if (postionImage == 2) {
            perview_image = grievance_image_two;
        } else if (postionImage == 3) {
            perview_image = grievance_image_three;
        } else if (postionImage == 4) {
            perview_image = grievance_image_four;
        } else if (postionImage == 5) {
            perview_image = grievance_image_five;
        }
        Intent intent = new Intent(this, PreviewImageActivity.class);
        startActivity(intent);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void dialogSelect(int imagePosition) {
        DialogSelect notifiDelete = new DialogSelect(this, imagePosition);
        notifiDelete.show();
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_CATEGORY_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    category = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (category.getStatus().equalsIgnoreCase("true")) {
                        adaptor = new CategoryAdaptor(context, category.getGrievanceCategoryList(), selectedLanguage);
                        spinner_category.setAdapter(adaptor);
                    }
                    return true;

                case ResReqController.GET_SUB_CATEGORY_SUCCESS:

                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();

                    sub_category = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (sub_category.getStatus().equalsIgnoreCase("true")) {
                        subCategoryAdaptor = new GrievacnceSubCategoryAdaptor(context, sub_category.getGrievanceSubCategoryList(), selectedLanguage);
                        spinner_sub_category.setAdapter(subCategoryAdaptor);
                    }
                    return true;

                case ResReqController.GET_CATEGORY_FAILED:
                    Toast.makeText(GrievancesActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.GET_SUB_CATEGORY_FAILED:
                    Toast.makeText(GrievancesActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.SUB_GRIEVANCE_SUCCESS:
                    GsonBuilder gsonBuilder_reg = new GsonBuilder();
                    Gson gson_reg = gsonBuilder_reg.create();

                    sub_category = gson_reg.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (sub_category.getStatus().equalsIgnoreCase("true")) {
                        onBackPressed();
                        Toast.makeText(context, context.getString(R.string.toast_grievance_posted_successfully), Toast.LENGTH_SHORT).show();

                    }
                    return true;

                case ResReqController.SUB_GRIEVANCE_FAILED:
                    Toast.makeText(GrievancesActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}
