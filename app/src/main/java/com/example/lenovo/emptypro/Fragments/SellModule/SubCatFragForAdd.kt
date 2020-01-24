
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lenovo.emptypro.Activities.MainActivity
import com.example.lenovo.emptypro.Fragments.SubCatFrag
import com.example.lenovo.emptypro.Fragments.SubCateBaseAdd
import com.example.lenovo.emptypro.Listeners.OnFragmentInteractionListener
import com.example.lenovo.emptypro.ModelClasses.AllApiResponse
import com.example.lenovo.emptypro.R
import com.example.lenovo.emptypro.Utilities.Utilities
import com.iww.classifiedolx.Fragments.AddAdvertise.FieldForAddNewAdvertise
import com.iww.classifiedolx.recyclerview.setUp
import kotlinx.android.synthetic.main.fragment_sub_cat_frag_for_add.*
import kotlinx.android.synthetic.main.item_sub_category_lst.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SubCatFragForAdd : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.tv_skipCat->
            {
                advertiseAddRequestParams!!.CatId=mainCatId
                advertiseAddRequestParams!!.CatName=mainCatTitle
                advertiseAddRequestParams!!.SubCatId=""
                advertiseAddRequestParams!!.SubCatName=""

                utility.enterNextReplaceFragment(
                        R.id.fl_subCateForAdd,
                        FieldForAddNewAdvertise.newInstance(advertiseAddRequestParams!!,""),
                        (ctx as MainActivity).supportFragmentManager
                )

            }
        }


     }

    // TODO: Rename and change types of parameters
      private var listener: OnFragmentInteractionListener? = null
      private var mainCatId: String? = null
    private var mainCatTitle: String? = null

    var ctx: Context? = null
    var TAG="SubCatFrag "
    var subCatItemData: MutableList<AllApiResponse.CategoryResponse.CategorySubListModel>? = null
    var strChoosedMainCat=""
    var utility = Utilities()
    var advertiseAddRequestParams:  AllApiResponse.AddAdvertiseRequest?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
   var str=it.getString(ARG_PARAM2)
            var strNameAndId= str.split("~~")
            mainCatId= strNameAndId[0]
            mainCatTitle= strNameAndId[1]
  }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_cat_frag_for_add, container, false)

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        subCatItemData = mutableListOf()
        advertiseAddRequestParams= AllApiResponse.AddAdvertiseRequest()

        rv_subCatForAdd.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_subCatForAdd.layoutManager = mLayoutManager
        tv_choosedMainCat.text=""+strChoosedMainCat
        rv_subCatForAdd.setUp(subCatItemData!!, R.layout.item_sub_category_lst, { it1 ->
            this.tv_subCatTitle.text = it1.subCategoryTitle

            this.ll_subCatItem.setOnClickListener {

                advertiseAddRequestParams!!.CatId=mainCatId
                advertiseAddRequestParams!!.CatName=mainCatTitle
                advertiseAddRequestParams!!.SubCatId=""+it1.subID
                advertiseAddRequestParams!!.SubCatName=it1.subCategoryTitle

                utility.enterNextReplaceFragment(
                        R.id.fl_subCateForAdd,
                        FieldForAddNewAdvertise.newInstance(advertiseAddRequestParams!!,""),
                        (ctx as MainActivity).supportFragmentManager
                )
            }
        }, { view1: View, i: Int -> })
        utility = Utilities()
        if(!utility .isConnected(ctx!!))
            utility .snackBar(rv_subCatForAdd, "Please check internet connection ")

        rv_subCatForAdd.adapter!!.notifyDataSetChanged()
        rv_subCatForAdd.layoutManager = LinearLayoutManager(context)

        swipe_refresh.setOnRefreshListener {
            if(!utility .isConnected(ctx!!))
                utility .snackBar(rv_subCatForAdd, "Please check internet connection ")
            //   fetchAllSubCat("get_subcategory")
            swipe_refresh.isRefreshing=false
        }
        if(!(subCatItemData!!.size>0))
        {
            tvNoData.visibility=View.VISIBLE
            rv_subCatForAdd.visibility=View.GONE
        }
        tv_choosedMainCat.text=mainCatTitle

        tv_skipCat.setOnClickListener(this)
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
        @JvmStatic
        fun newInstance(param1: MutableList<AllApiResponse.CategoryResponse.CategorySubListModel> , param2: String) =
                SubCatFragForAdd().apply {
                    arguments = Bundle().apply {
                        subCatItemData=param1
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
