package com.example.lenovo.emptypro.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses.GetDataService
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse
import com.example.lenovo.emptypro.R
import com.example.lenovo.emptypro.Utilities.Utilities
import com.example.lenovo.emptypro.Utils.GlobalData
import com.example.lenovo.emptypro.Utils.SharedPrefUtil
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_p2_pchat_view.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class P2PChatView : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.img_back -> {
                onBackPressed()
            }
            R.id.fl_sendMsg -> {
                if (et_chatMsg.text.toString().trim() != "")
                    addChatMessage()
                else
                    et_chatMsg.setError("Enter message")
            }
        }
    }

    private fun addChatMessage() {

        var chatRef:DatabaseReference?=null
        chatRef = FirebaseDatabase.getInstance().reference.child("/AllChat").child(""+petBaseChatId)

        if(petBaseChatId=="") {
            chatRef = FirebaseDatabase.getInstance().reference.child("/AllChat")
     petBaseChatId = ""+chatRef.push().key

            chatRef = FirebaseDatabase.getInstance().reference.child("/AllChat").child(""+petBaseChatId)
            chatRef!!.child("petName" ).setValue("" )
            chatRef!!.child("petId" ).setValue(""+petId )
            chatRef!!.child("petImage" ).setValue("" )
            chatRef!!.child("ownerId" ).setValue(ownerId )
            chatRef!!.child("intersterId" ).setValue(""+SharedPrefUtil.getUserId(this@P2PChatView) )

        }
        val msgId = chatRef.push().key

         chatRef!!.child("" + msgId).child("sender").setValue("" +SharedPrefUtil.getUserId(this@P2PChatView))
        chatRef.child("" + msgId).child("reciever").setValue("" + ownerId)
        chatRef.child("" + msgId).child("time").setValue(ServerValue.TIMESTAMP)
        chatRef.child("" + msgId).child("message").setValue("" + et_chatMsg.text.toString())
        et_chatMsg.setText("")
    }

    internal var service: GetDataService? = null
    var TAG = "P2PChatView "
    internal var utilities = Utilities()
    var petId=""
    var petImg=""
    var ownerId=""
    var petBaseChatId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2_pchat_view)
        fl_sendMsg.setOnClickListener(this)
        img_back.setOnClickListener(this)
          /*  var loggeInRef = FirebaseDatabase.getInstance().getReference().child("/chat")
            val userId = loggeInRef.push().key
            loggeInRef.child("" + userId).child("name").setValue("strName")
            loggeInRef.child("" + userId).child("email").setValue("strEmail")
            loggeInRef.child("" + userId).child("mobile").setValue("strMob")
            loggeInRef.child("" + userId).child("password").setValue("strPsd")
            loggeInRef.child("" + userId).child("userType").setValue("User")
            loggeInRef.child("" + userId).child("status").setValue("active")*/
        //getPetDetail()
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
        var dialog= utilities.dialog(this@P2PChatView )
        Log.e(TAG + " getPetDetail", "single-pet/?userID=" + SharedPrefUtil.getUserId(this@P2PChatView) + "&petID=" + petId)
        val call = service!!.getPetDetailsApi(SharedPrefUtil.getUserId(this@P2PChatView), "" + petId)

        call.enqueue(object : Callback<AllApiResponse.PetDetailRes> {
            override fun onResponse(call: Call<AllApiResponse.PetDetailRes>, response: Response<AllApiResponse.PetDetailRes>) {
                Log.e(TAG + " getPetDetail", "response   $response")
                dialog.hide()
                if (response.body()!!.status.equals("200") && response.body()!!.data.size > 0) {
                     Log.e(TAG + " getPetDetail", "size=" + response.body()!!.data.size)
ownerId=response.body()!!.data[0].userID
                    if(response.body()!!.data[0].images.size>0) {
                        petImg = response.body()!!.data[0].images[0].img
                    }
                        getOldChat(ownerId,SharedPrefUtil.getUserId(this@P2PChatView),petId )

                } else {
                    GlobalData.showSnackbar(this@P2PChatView as Activity, response.body()!!.messageType)
                }
            }




            override fun onFailure(call: Call<AllApiResponse.PetDetailRes>, t: Throwable) {
                dialog.hide()
                 // progress_bar.setVisibility(View.GONE);
                Toast.makeText(this@P2PChatView, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getOldChat (strOwnerId: String, strIntersterId: String,strPetId: String) {
        Log.e(TAG + " ", "function searching  getOldChat()")

        var allUsersListener: ValueEventListener? = null
        var loggeInRef = FirebaseDatabase.getInstance().getReference().child("/AllChat")


        allUsersListener = object : ValueEventListener {
            @SuppressLint("WrongConstant")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e(TAG + " ", "function Calling  getOldChat()")

                val children = dataSnapshot!!.children

                var txtTest = "searching data"
                children.forEach {

                //    var strImg= it.child("profileImg").value.toString()
                    var strDB_OwnerId = it.child("ownerId").value.toString()
                    var strDB_PetId= it.child("petId").value.toString()
                    var strDB_IntersterId= it.child("intersterId").value.toString()
                    Log.e(
                            TAG + " getOldChat",
                            "  ownerId=" + it.child("ownerId").value.toString() + "    petId=" + it.child("petId").value.toString()+"   "+"intersterId="+it.child("intersterId").value.toString()
                    )


                    if (strDB_OwnerId == strOwnerId && strDB_IntersterId== strIntersterId && strDB_PetId==strPetId ) {
                        petBaseChatId = ""+it.key
                   /*     var strDBMob = it.child("mobile").value.toString()
                        var strDBName = it.child("name").value.toString()
                        var strDB_status= it.child("status").value.toString()*/
                      /*  if(strDB_status.toLowerCase().equals("active"))
                        {
                            userLoginMsg = "User Login Successfully"
                            sharedPref!!.userName = strDBName
                            sharedPref!!.userMob = strDBMob
                            sharedPref!!.userEmail = strDBEmail
                            sharedPref!!.fireBaseUserId = userId
                            sharedPref!!.userImg=""+ strImg

                            loggeInRef.removeEventListener(allUsersListener!!)

                            return
                        }

                        else {
                            userLoginMsg = "User Not Activated ,please contact with Admin"

                        }*/
                         txtTest="matched data "
                        return

                    }
                    Log.e(TAG, "getOldChat  :  userLoginMsg" )
                }

                Toast.makeText(applicationContext, ""+txtTest, Toast.LENGTH_LONG).show()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e(TAG, "getOldChat  :onCancelled", databaseError.toException())

            }
        }
        loggeInRef.addValueEventListener(allUsersListener!!)



    }



}
