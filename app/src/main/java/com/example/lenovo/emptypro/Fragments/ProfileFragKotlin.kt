package com.example.lenovo.emptypro.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lenovo.emptypro.Activities.LoginSignUp
import com.example.lenovo.emptypro.Activities.MainActivity
import com.example.lenovo.emptypro.Activities.SplashScreen
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.GetDataService
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.RetrofitClientInstance
import com.example.lenovo.emptypro.Listeners.OnFragmentInteractionListener
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse
import com.example.lenovo.emptypro.R
import com.example.lenovo.emptypro.Utilities.Utilities
import com.example.lenovo.emptypro.Utils.SharedPrefUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
 private const val ARG_PARAM1 = "param1"
 private const val ARG_PARAM2 = "param2"

class ProfileFragKotlin : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
when(v!!.id)
{
    R.id.btn_updateProfile ->
    {
        if (btn_updateProfile.text.toString().equals("Submit")) {
            checkEditTextFieldVal()
        }
        if (btn_updateProfile.text.toString().equals("Update Profile")) {
            et_firstName.isEnabled = true
            et_lastName.isClickable = true
            et_PhoneNum.isEnabled = false
            tv_city.isClickable = true
            et_village.isEnabled = true
            et_state.isEnabled = true
            et_email.isEnabled = true
            et_address.isEnabled = true
            btn_updateProfile.text = "Submit"
        }
    }
}
    }

    private fun checkEditTextFieldVal() {
        var strFName = "" + et_firstName.text.toString()
        var strLName = "" + et_lastName.text.toString()
        var strState = "" + et_state.text.toString()
        var strCity = "" + tv_city.text.toString()
        var strEmail = "" + et_email.text.toString()
        var strVillage= "" + et_village.text.toString()
        var strAddress= "" + et_address.text.toString()


        if (strFName.replace(" ".toRegex(),"").equals("")) {
            utility.snackBar(et_firstName, "Enter First Name")
            et_firstName.isFocusable = true
        } else if (strLName.replace(" ".toRegex(),"").equals("")) {
            utility.snackBar(et_firstName, "Enter First Name")
            et_lastName.isFocusable = true
        } else if (strState.replace(" ".toRegex(),"").equals("")) {
            utility.snackBar(et_firstName, "Enter First Name")
            et_state.isFocusable = true
        } else if (strCity.replace(" ".toRegex(),"").equals("")) {
            utility.snackBar(et_firstName, "Enter First Name")
             tv_city.setTextColor(Color.RED)
        }
        else if (strEmail.replace(" ".toRegex(),"").equals("")) {
            utility.snackBar(et_firstName, "Enter First Name")
            et_email.isFocusable = true
        }else if (strVillage.replace(" ".toRegex(),"").equals("")) {
            utility.snackBar(et_firstName, "Enter First Name")
            et_village.isFocusable = true
        }else if (strAddress.replace(" ".toRegex(),"").equals("")) {
            utility.snackBar(et_firstName, "Enter First Name")
            et_address.isFocusable = true
        }
        else{
            if(!utility .isConnected(ctx!!))
                utility .snackBar(ll_profile, "Please check internet connection ")
            else{
                updateProfile(strFName,strLName,strState,strCity,strEmail,strVillage,strAddress)
            }
        }

    }

    private fun updateProfile(strFName: String, strLName: String, strState: String, strCity: String, strEmail: String, strVillage: String, strAddress: String) {

        Log.e(TAG + "call updateProfile", "firstName=" +strFName+" &lastName="+strLName+" &address="+strAddress+" &village="+strVillage+" &city="+strCity+" &state="+strState+" &Email="+strEmail+" &phone="+SharedPrefUtil.getUserMobile(ctx)+" &userID"+SharedPrefUtil.getUserId(ctx))

        var dialogBar=utility.dialog(context!!)
        val call = service!!.profileUpdateApi(""+strFName,""+strLName,""+strAddress,""+strVillage,""+strCity,""+strState,""+strEmail,""+SharedPrefUtil.getUserMobile(ctx),""+SharedPrefUtil.getUserId(ctx))

        call.enqueue(object : Callback<AllApiResponse.UserProfileDetailRes> {
            override fun onResponse(call: Call<AllApiResponse.UserProfileDetailRes>, response: Response<AllApiResponse.UserProfileDetailRes>) {
                Log.e(TAG + " updateProfile", "" + Gson().toJson(response.body()))
                dialogBar.hide()

                if( response.body()!!.status.equals("200") /*&& response.body()!!.data.size>0*/) {
                   /* //var dataModel=response.body()!!.data[0]
                    et_firstName.setText("" +dataModel.firstName)
                    et_lastName.setText(""+dataModel.lastName)
                    et_state.setText(""+dataModel.state)
                    tv_city.setText(""+dataModel.city)
                    et_email.setText(""+dataModel.email)
                    et_village.setText(""+dataModel.village)
                    et_address.setText(""+dataModel.address)
//                    SharedPrefUtil.setUserId(ctx, "" +dataModel.)
                    SharedPrefUtil.setUserMobile(ctx, "" + dataModel.phone)*/
                    var intentLoginSign =  Intent(context, MainActivity::class.java)
                    startActivity(intentLoginSign);
                    (  context as Activity ).finish()
                }
            }

            override fun onFailure(call: Call<AllApiResponse.UserProfileDetailRes>, t: Throwable) {
                dialogBar.hide()
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    // TODO: Rename and change types of parameters
     private var listener: OnFragmentInteractionListener? = null
      private var mainCatId: String? = null
    var ctx: Context? = null
    var TAG="ProfileFragKotlin "
    internal var service: GetDataService? = null

    var cityArrayList: List<AllApiResponse.CityResponse.CityModel>? = null

    var utility = Utilities()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
          //  param1 = it.getString(ARG_PARAM1)
            mainCatId= it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

        if(!utility .isConnected(ctx!!))
                utility .snackBar(ll_profile, "Please check internet connection ")
     else{
                 fetchProfile()
                 getAllCities()

             }

        et_firstName.isEnabled = false
        et_lastName.isClickable = false
        et_PhoneNum.isEnabled = false
        tv_city.isClickable = false
        et_village.isEnabled = false
        et_state.isEnabled = false
        et_email.isEnabled = false
        et_address.isEnabled = false
        btn_updateProfile.text = "Update Profile"
        btn_updateProfile.setOnClickListener(this)
    }
    private fun fetchProfile() {
        Log.e(TAG,"fetchProfile   = /profile-data//"+SharedPrefUtil.getUserId(ctx) )

        val call = service!!.getProfileInfo(SharedPrefUtil.getUserId(ctx))

        call.enqueue(object : Callback<AllApiResponse.UserProfileDetailRes> {
            override fun onResponse(call: Call<AllApiResponse.UserProfileDetailRes>, response: Response<AllApiResponse.UserProfileDetailRes>) {
                Log.e(TAG + " fetchProfile", "" + Gson().toJson(response.body()))


                if( response.body()!!.status.equals("200") && response.body()!!.data.size>0) {
                      var dataModel=response.body()!!.data[0]
                     et_firstName.setText("" +dataModel.firstName)
                     et_lastName.setText(""+dataModel.lastName)
                     et_state.setText(""+dataModel.state)
                     tv_city.setText(""+dataModel.city)
                     et_email.setText(""+dataModel.email)
                     et_village.setText(""+dataModel.village)
                     et_address.setText(""+dataModel.address)
 //                    SharedPrefUtil.setUserId(ctx, "" +dataModel.)
                     SharedPrefUtil.setUserMobile(ctx, "" + dataModel.phone)
                }
                else
                {
                    utility.snackBar(et_firstName,"Please try again")
                }
            }

            override fun onFailure(call: Call<AllApiResponse.UserProfileDetailRes>, t: Throwable) {
                // progress_bar.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun getAllCities() {
        Log.e(TAG,"getAllCities   = /city/" )
        var dialogBar=utility.dialog(context!!)

        service!!.allCityListApi( ).enqueue(object : Callback<AllApiResponse.CityResponse> {
            override fun onResponse(
                    call: Call<AllApiResponse.CityResponse>,
                    response: Response<AllApiResponse.CityResponse>
            ) {
                dialogBar.cancel()
                Log.e("getAllCities res", "" + Gson().toJson(response.body()))
                if (response.isSuccessful && (response.body()!!.status.equals("200"))) {

                    cityArrayList = response.body()!!.data

                } else {
                    //swipe_refresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<AllApiResponse.CityResponse>, t: Throwable) {
                t.printStackTrace()
                dialogBar.cancel()

                //swipe_refresh.isRefreshing = false
            }
        })



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
      ctx=context
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String , param2: String) =
                SubCatFrag().apply {
                    arguments = Bundle().apply {
                         putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
