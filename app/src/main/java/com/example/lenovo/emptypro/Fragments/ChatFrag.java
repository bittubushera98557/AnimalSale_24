package com.example.lenovo.emptypro.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lenovo.emptypro.Listeners.OnFragmentInteractionListener;
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse.ChatModel;
import com.example.lenovo.emptypro.Adapters.ChatListAdap;
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse;
import com.example.lenovo.emptypro.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
SwipeRefreshLayout swpRL_chat;
View rootView;
    List<AllApiResponse.ChatModel> allChatList = new ArrayList<>();
ChatListAdap chatListAdap;
RecyclerView rv_chat;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

     private OnFragmentInteractionListener mListener;

    public ChatFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFrag newInstance(String param1, String param2) {
        ChatFrag fragment = new ChatFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_chat, container, false);
    initView();
    setListData();
        return  rootView;
    }

    private void setListData() {
        allChatList.clear();
          ChatModel chatModel= new  ChatModel();
         chatModel.setStatus("1");
        chatModel.setName("abc singh");

        allChatList.add (chatModel);
        chatModel=new  ChatModel();
        chatModel.setStatus("1");
chatModel.setName("xyz user");
        allChatList.add (chatModel);
        chatModel=new AllApiResponse.ChatModel();
        chatModel.setStatus("1");
        chatModel.setName("abc singh");
        allChatList.add (chatModel);
        chatListAdap.addingJobsData(allChatList);
        Log.e("size items",""+allChatList.size());
    }

    private void initView() {
        rv_chat=rootView.findViewById(R.id.rv_chat);
        swpRL_chat=rootView.findViewById(R.id.swpRL_chat);
        chatListAdap= new ChatListAdap(getActivity());
        rv_chat.setItemViewCacheSize(20);
        rv_chat.setDrawingCacheEnabled(true);
        rv_chat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rv_chat.setAdapter(chatListAdap);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
