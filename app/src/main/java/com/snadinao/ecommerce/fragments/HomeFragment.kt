package com.snadinao.ecommerce.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.snadinao.ecommerce.model.CategoryData
import com.snadinao.ecommerce.R
import com.snadinao.ecommerce.adapter.CategoryAdapter
import com.snadinao.ecommerce.adapter.SliderAdapter
import com.snadinao.ecommerce.databinding.FragmentHomeBinding
import com.snadinao.ecommerce.model.SliderItem
import com.snadinao.ecommerce.model.User
import com.snadinao.ecommerce.util.categoryItemList
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlin.math.abs

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var catList: ArrayList<CategoryData>
    private lateinit var catAdapter: CategoryAdapter
    private lateinit var catRecycler: RecyclerView
    private lateinit var viewPagerImageSlider: ViewPager2
    private lateinit var sliderItemList: ArrayList<SliderItem>
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRun: Runnable
    var firebaseFireStore: FirebaseFirestore? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menuDrawer: ImageView
    private lateinit var naviget: NavigationView
    var user: User? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryData(view)
        init(view)
        sliderView()
    }

    private fun init(view: View){
        drawerLayout = view.findViewById(R.id.mainDrawer)
        menuDrawer = view.findViewById(R.id.profileImage)
        naviget = view.findViewById(R.id.navDrawer)
        viewPagerImageSlider = view.findViewById(R.id.viewPagerImageSlider)
        openDrawer()
        categoryData(view)
        sliderView()
    }

    private fun openDrawer() {
        menuDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        naviget.itemIconTintList = null
        database = FirebaseDatabase.getInstance()
        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("CheckResult")
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)
                    if (user?.uid.equals(FirebaseAuth.getInstance().uid)){
                        Glide.with(view!!.context)
                            .load(user?.phoneImage)
                            .placeholder(R.drawable.avatar)
                            .into(userImage1)
                        Glide.with(requireActivity())
                            .load(user?.phoneImage)
                            .placeholder(R.drawable.avatar)
                            .into(profileImage)
                        userName1.text = user?.name
                        userMob1.text = user?.phoneNumber
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            } )
    }

    private fun sliderView() {
        sliderItemList = ArrayList()
        firebaseFireStore = FirebaseFirestore.getInstance()
        firebaseFireStore!!.collection("BANNER").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (documentSnapShot in task.result) {
                        val sliderItem = SliderItem(documentSnapShot.get("sliderImage").toString())
                        sliderItemList.add(sliderItem)
                    }
                }
            }

        sliderAdapter = SliderAdapter(requireActivity(), viewPagerImageSlider, sliderItemList)
        viewPagerImageSlider.adapter = sliderAdapter
        viewPagerImageSlider.clipToPadding = false
        viewPagerImageSlider.clipChildren = false
        viewPagerImageSlider.offscreenPageLimit = 3
        viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val composPageTarn = CompositePageTransformer()
        composPageTarn.addTransformer(MarginPageTransformer(40))
        composPageTarn.addTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPagerImageSlider.setPageTransformer(composPageTarn)
        sliderHandler = Handler()
        sliderRun = Runnable { viewPagerImageSlider.currentItem = viewPagerImageSlider.currentItem + 1 }
        viewPagerImageSlider.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRun)
                sliderHandler.postDelayed(sliderRun, 3000)
            }
        } )
    }

    private fun categoryData(view: View) {
        catRecycler = view.findViewById(R.id.categoryRecycler)
        catList = ArrayList()
        categoryItemList(catList)
        catAdapter = CategoryAdapter(view.context, catList)
        catRecycler.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        catRecycler.adapter = catAdapter
    }

}

