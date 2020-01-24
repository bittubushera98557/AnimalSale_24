package com.example.lenovo.emptypro.ApiCallClasses.RetrofitClasses;

import android.provider.ContactsContract;

import com.example.lenovo.emptypro.ModelClasses.AllApiResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GetDataService {

     @GET("category/")
    Call<AllApiResponse.CategoryResponse> allCateListApi();

    @GET("city/")
    Call<AllApiResponse.CityResponse> allCityListApi();

    @FormUrlEncoded
    @POST("all-pets/")
    Call<AllApiResponse.AllTypePetsRes> allTypePetsListApi(@Field("cityName") String cityName,@Field("userID") String userID);

    @FormUrlEncoded
    @POST("sold/")
    Call<AllApiResponse.SoldPetsRes> getSoldPetsApi(@Field("userID") String userID);

    @FormUrlEncoded
    @POST("uploaded/")
    Call<AllApiResponse.UploadedPetsRes> getUploadedPetsApi(@Field("userID") String userID);

    @FormUrlEncoded
    @POST("favourite/")
    Call<AllApiResponse.FavouritePetsRes> getFavouritePetsApi(@Field("userID") String userID);

    @FormUrlEncoded
    @POST("filter-data/")
    Call<AllApiResponse.FilterBasePetsRes> getFilterBaseApi(@Field("userID") String userID,@Field("cityName") String cityName,@Field("catID") String catID,@Field("subCat") String subCat);

    @FormUrlEncoded
    @POST("single-pet/")
    Call<AllApiResponse.PetDetailRes> getPetDetailsApi(@Field("userID") String userID,@Field("petID") String petID);


    @FormUrlEncoded
    @POST("generateOTP/")
     Call<AllApiResponse.OtpResponse> getOtpApi(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("verifyOTP/")
    Call<AllApiResponse.VerifyOtpRes> verifyOtpApi(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("favourite-set/")
    Call<AllApiResponse.CommonRes> addInFav(@Field("action") String action, @Field("userID") String userId,@Field("petID") String petId);   //action=sold,add,remove


    @FormUrlEncoded
    @POST("feedback/")
    Call<AllApiResponse.CommonRes> feedbackApi(@Field("userID") String userid, @Query("message") String message);

    @FormUrlEncoded
    @POST("profile-data//")
    Call<AllApiResponse.UserProfileDetailRes> getProfileInfo(@Field("userID") String userID);

    @FormUrlEncoded
    @POST("profile-update/")
    Call<AllApiResponse.UserProfileDetailRes> profileUpdateApi(@Field("firstName") String firstName, @Field("lastName") String lastName, @Field("address") String address,
                                                    @Field("village") String village, @Field("city") String city ,@Field("state") String state,
                                                    @Field("Email") String Email ,@Field("phone") String phone ,@Field("userID") String userid );
//firstName, lastName, address, village, city, state, Email, phone, userID

    @Multipart
    @POST("sell-pets/")
    Call<AllApiResponse.CommonRes> addNewPetApi(@Query("mainCat") String mainCat,
                                                          @Query("subCat") String subCat,
                                                          @Query("petName") String petName,
                                                          @Query("petTitle") String petTitle,
                                                          @Query("petBreed") String petBreed,
                                                          @Query("petAge") String petAge,
                                                          @Query("petPrice") String petPrice,
                                                          @Query("petDescription") String petDescription,
                                                          @Query("dollar") String dollar,
                                                          @Query("action") String action,
                                                @Query("petID") String petID,

                                                          @Query("userID") String userID,
                                                            @Part MultipartBody.Part img1, @Part MultipartBody.Part img2,
                                                          @Part MultipartBody.Part img3, @Part MultipartBody.Part img4,
                                                @Part MultipartBody.Part img5, @Part MultipartBody.Part img6,
                                                @Part MultipartBody.Part img7, @Part MultipartBody.Part img8
                                                );


    @FormUrlEncoded
    @POST("sell-pets/")
    Call<AllApiResponse.CommonRes> addNewPetWithOutImgApi(@Field("mainCat") String mainCat,
                                                @Field("subCat") String subCat,
                                                @Field("petName") String petName,
                                                @Field("petTitle") String petTitle,
                                                @Field("petBreed") String petBreed,
                                                @Field("petAge") String petAge,
                                                @Field("petPrice") String petPrice,
                                                @Field("petDescription") String petDescription,
                                                @Field("dollar") String dollar,
                                                @Field("action") String action,
                                                @Field("petID") String petID,

                                                @Field("userID") String userID);
//mainCat, subCat, petName, petTitle, petBreed, petAge, petPrice, petDescription, dollar, action, petID, userID, img1- img8


}
