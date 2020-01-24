package com.example.lenovo.emptypro.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.GetDataService
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.RetrofitClientInstance
import com.example.lenovo.emptypro.Fragments.AdvertisementDetailsFrag
import com.example.lenovo.emptypro.Listeners.OnFragmentInteractionListener
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse
import com.example.lenovo.emptypro.R
import com.example.lenovo.emptypro.Utilities.Utilities
import com.example.lenovo.emptypro.Utils.GlobalData
import com.example.lenovo.emptypro.Utils.SharedPrefUtil
import com.google.gson.Gson
import com.iww.classifiedolx.recyclerview.setUp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_uploaded_pets.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.home_searched_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UploadedPets : AppCompatActivity(), View.OnClickListener, OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri?) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                onBackPressed()
            }

        }
    }

    private var allUploadedPetsLst: MutableList<AllApiResponse.UploadedPetsRes.UploadedPetsItem>? = null
    //    var ctx: Context?=null
    internal var service: GetDataService? = null
    var TAG = "UploadedPets "
    var utilities = Utilities()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploaded_pets)
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        allUploadedPetsLst = mutableListOf()
        img_back.setOnClickListener(this)
        tv_title.text = "Uploaded Pets"

        rv_uploadedPets.setHasFixedSize(true);
        rv_uploadedPets.layoutManager = GridLayoutManager(this@UploadedPets, 2)

        rv_uploadedPets.setUp(allUploadedPetsLst!!, R.layout.home_searched_item, { it1 ->
            val circularProgressDrawable = CircularProgressDrawable(this@UploadedPets)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Log.e(TAG + "", "UploadedPet   name=" + it1.petName + "  " + it1.category)
            if (it1.images.size > 0)
                Picasso.with(context).load(it1.images[0].img).fit().placeholder(circularProgressDrawable).error(R.drawable.app_logo).into(iv_searchedItemImg)

            this.tv_searchedItemShortInfo.text = "Rs. " + it1.petPrice

            var strInfo=""
            if(!it1.petTitle.equals("") && !it1.petTitle.equals("null")  )
                strInfo=strInfo+" | "+it1.petTitle
            if(!it1.category.equals("") && !it1.category.equals("null")  )
                strInfo=strInfo+" | "+it1.category
            if(!it1.subCategory.equals("") && !it1.subCategory.equals("null")  )
                strInfo=strInfo+" | "+it1.subCategory
            this.tv_searchedItemLongInfo.text =  strInfo.substring(2)
            tv_searchedItemLoc.text=it1.userCity+", "+it1.userState
            tv_searchedItemDate.text=GlobalData.dateSplit(it1.createdOn)
            iv_favImg.visibility = View.GONE
            if (ll_searchedItem != null) {
                val display = (this@UploadedPets).windowManager.defaultDisplay
                ll_searchedItem.getLayoutParams().width = Math.round((display.width / 2).toFloat())
            }
            ll_searchedItem.setOnClickListener {


                val intent = Intent(this@UploadedPets, PetDetailsActivity::class.java)
                intent.putExtra("oldPetId", "" + it1.petID)
                startActivity(intent)

            }
        }, { view1: View, i: Int -> })
        rv_uploadedPets.layoutManager = GridLayoutManager(this@UploadedPets, 2)
        if (GlobalData.isConnectedToInternet(this@UploadedPets)) {
            callAllUploadedPetsApi()
        } else {
            GlobalData.showSnackbar(this@UploadedPets, getString(R.string.please_check_your_internet_connection))
        }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing=false
            if (GlobalData.isConnectedToInternet(this@UploadedPets)) {
                callAllUploadedPetsApi()
            } else {
                GlobalData.showSnackbar(this@UploadedPets, getString(R.string.please_check_your_internet_connection))
            }
        }

    }

    private fun callAllUploadedPetsApi() {

        swipe_refresh.isRefreshing=true

        val call = service!!.getUploadedPetsApi(SharedPrefUtil.getUserId(this@UploadedPets))

        call.enqueue(object : Callback<AllApiResponse.UploadedPetsRes> {
            override fun onResponse(call: Call<AllApiResponse.UploadedPetsRes>, response: Response<AllApiResponse.UploadedPetsRes>) {
                Log.e(TAG + " UploadedPetsApi", "response "+ Gson().toJson(response.body()));
                swipe_refresh.isRefreshing=false
                if (response.body()!!.status.equals("200") && (response.body()!!.data.size>0)) {
                    allUploadedPetsLst!!.clear()
                    allUploadedPetsLst!!.addAll(response.body()!!.data)
                    Log.e(TAG + " UploadedPetsApi", "size=" + response.body()!!.data.size)

                    rv_uploadedPets.adapter!!.notifyDataSetChanged()
                    tvNoData.visibility=View.GONE
                }
                else{
                    tvNoData.visibility=View.VISIBLE
                }
            }

            override fun onFailure(call: Call<AllApiResponse.UploadedPetsRes>, t: Throwable) {
                swipe_refresh.isRefreshing=false
                Toast.makeText(this@UploadedPets, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onBackPressed() {
        val intent = Intent(this@UploadedPets, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
