package com.example.lenovo.emptypro.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.GetDataService
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.RetrofitClientInstance
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse
import com.example.lenovo.emptypro.R
import com.example.lenovo.emptypro.Utils.SharedPrefUtil
import com.glide.slider.library.SliderTypes.TextSliderView
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.lenovo.emptypro.Listeners.OnFragmentInteractionListener
import com.example.lenovo.emptypro.Utilities.Utilities
import com.example.lenovo.emptypro.Utils.GlobalData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_advertisement_details.*
import kotlinx.android.synthetic.main.header.*
import java.lang.Exception

class PetDetailsActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap?) {

        googleMap!!.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        gMap = googleMap
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
onBackPressed()
            }
            R.id.tv_call -> {
utilities.callDialog(this@PetDetailsActivity,""+petDetailModel!!.userNumber)
            }
            R.id.fl_AdvertisementDetailsFrag -> {

            }
            R.id.tv_bottomChat-> {
                val intent = Intent(this@PetDetailsActivity, P2PChatView::class.java)
                intent.putExtra("oldPetId", "" + petId)
                startActivity(intent)
            }

        }
    }

    // TODO: Rename and change types of parameters
    private var petId: String? = ""
    var petDetailModel: AllApiResponse.PetDetailRes.PetDetail? = null
    var lati = "0"
    var longi = "0"
    internal var service: GetDataService? = null
    var gMap: GoogleMap? = null
    var TAG = "AdvertisementDetailsFrag "
    internal var utilities = Utilities()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_advertisement_details)
        ll_topHeaderLayout.visibility = View.VISIBLE
        img_back.setOnClickListener(this)
        tv_title.text = "Pet Detail"
        tv_bottomChat.setOnClickListener(this)
        tv_call.setOnClickListener(this)
        fl_AdvertisementDetailsFrag.setOnClickListener(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

        try {
            getOldIntentData()
        } catch (exp: Exception) {

        }
    }

    private fun getOldIntentData() {

        petId = intent.extras!!.getString("oldPetId")
        if (petId.equals(""))
            getPetDetail()

    }

    private fun getPetDetail() {
        Log.e(TAG + " getPetDetail", "single-pet/?userID=" + SharedPrefUtil.getUserId(this@PetDetailsActivity) + "&petID=" + petId)
        val call = service!!.getPetDetailsApi(SharedPrefUtil.getUserId(this@PetDetailsActivity), "" + petId)

        call.enqueue(object : Callback<AllApiResponse.PetDetailRes> {
            override fun onResponse(call: Call<AllApiResponse.PetDetailRes>, response: Response<AllApiResponse.PetDetailRes>) {
                Log.e(TAG + " getPetDetail", "response   $response")
                if (response.body()!!.status.equals("200") && response.body()!!.data.size > 0) {
                    petDetailModel = response.body()!!.data[0]
                    for (item in petDetailModel!!.images) {
                        val sliderView = TextSliderView(this@PetDetailsActivity)
                        sliderView.image(item.img).setProgressBarVisible(true)
                        slider.addSlider(sliderView)
                    }
                    tv_advertiseLoc.text = "" + petDetailModel!!.userCity + ", " + petDetailModel!!.userState
                    tv_advertiseDate.text = "" + petDetailModel!!.createdOn
                    tv_petTitle.text = petDetailModel!!.petTitle
                    tv_advertiseDesc.text = petDetailModel!!.petDescription
if(petDetailModel!!.userID.equals(SharedPrefUtil.getUserId(this@PetDetailsActivity)))
{
    ll_bottomChatCall.visibility= View.GONE

}

                    Log.e(TAG + " getPetDetail", "size=" + response.body()!!.data.size)

                } else {
                    GlobalData.showSnackbar(this@PetDetailsActivity as Activity, response.body()!!.messageType)
                }
            }

            override fun onFailure(call: Call<AllApiResponse.PetDetailRes>, t: Throwable) {
                // progress_bar.setVisibility(View.GONE);
                Toast.makeText(this@PetDetailsActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onBackPressed() {

        finish()
    }
}
