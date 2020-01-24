package com.example.lenovo.emptypro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.GetDataService;
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.RetrofitClientInstance;
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse;
import com.example.lenovo.emptypro.R;
import com.example.lenovo.emptypro.Utils.SharedPrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackScreen extends AppCompatActivity implements View.OnClickListener {
Button btn_submitFeedback;
EditText et_feedback;
    GetDataService service;
    String TAG="FeedbackScreen ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_screen);
        et_feedback=(EditText) findViewById(R.id.et_feedback);
                btn_submitFeedback=(Button)findViewById(R.id.btn_submitFeedback);

        btn_submitFeedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_submitFeedback:
            {
                String strFeedBack=et_feedback.getText().toString();
                if(strFeedBack.replaceAll(" ","").equals(""))
                {
                    Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    callFeedBackAPI(strFeedBack);
                }
            }
        }
    }

    private void callFeedBackAPI(String strFeedBack) {
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<AllApiResponse.CommonRes> call = service.feedbackApi(""+SharedPrefUtil.getUserId(FeedbackScreen.this),""+strFeedBack);

        call.enqueue(new Callback<AllApiResponse.CommonRes>() {
            @Override
            public void onResponse(Call<AllApiResponse.CommonRes> call, Response<AllApiResponse.CommonRes> response) {
                 Log.e(TAG+" callGetOtpApi","response   "+   response .toString());

                if (response.isSuccessful() && response.body().messageType.equals("success")) {
               et_feedback.setText("");

                }
         //       Toast.makeText(FeedbackScreen.this, ""+response.body().message, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<AllApiResponse.CommonRes> call, Throwable t) {
                // progress_bar.setVisibility(View.GONE);
                Toast.makeText(FeedbackScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
        finish();
    }
}
