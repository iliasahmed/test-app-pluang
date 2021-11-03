package com.iliasahmed.testpluang.ui.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.iliasahmed.testpluang.R
import com.iliasahmed.testpluang.data.PreferenceRepository
import com.iliasahmed.testpluang.databinding.FragmentHomeBinding
import com.iliasahmed.testpluang.model.QuotesModel
import com.iliasahmed.testpluang.rest.ApiRepository
import com.iliasahmed.testpluang.ui.base.BaseFragment
import com.iliasahmed.testpluang.utils.ViewDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    HomeViewModel::class.java,
    R.layout.fragment_home
) {

    @Inject lateinit var preferenceRepository: PreferenceRepository

    private var list: ArrayList<QuotesModel>? = null
    private var adapter: HomeAdapter? = null
    private var manager: LinearLayoutManager? = null
    private var loading: ViewDialog? = null

    @SuppressLint("CheckResult")
    override fun initViews() {
        list = ArrayList<QuotesModel>()
        loading = ViewDialog(activity)
        loading!!.showDialog()
        viewModel.getQuotes()

        Observable.timer(5000, TimeUnit.MILLISECONDS)
            .repeat()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.getQuotes()
            }

        binding.toolbar.menu.getItem(0).setOnMenuItemClickListener {
            if (adapter != null){
                if (adapter!!.map.size > 0){
                    if (preferenceRepository.email.equals("")){
                        Toast.makeText(context, "Please login to save to wishlist", Toast.LENGTH_SHORT).show()
                    }else {
                        loading!!.showDialog()
                        viewModel.saveToFirestore(adapter!!.map)
                    }
                }else{
                    Toast.makeText(context, "No item selected for wishlist", Toast.LENGTH_SHORT).show()
                }
            }
            false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun liveEventsObservers() {
        viewModel.liveList.observe(viewLifecycleOwner) { wishListEntities ->
            list!!.clear()
            list!!.addAll(wishListEntities)
            adapter!!.notifyDataSetChanged()
            loading!!.hideDialog()
        }

        viewModel.saved.observe(viewLifecycleOwner){
            loading!!.hideDialog()
            if (it){
                adapter!!.map.clear()
                adapter!!.notifyDataSetChanged()
                Toast.makeText(context, "Saved to wishlist", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Something went wrong, can't save!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setupRecycler() {
        manager = LinearLayoutManager(context)
        binding.recycle.layoutManager = manager
        adapter = HomeAdapter(list, context, object :
            HomeAdapter.WishListListener {
            override fun checkEmpty() {}
            override fun delete(slug: String?) {
//                viewModel.deleteBySid(slug)
            }
        })
        binding.recycle.adapter = adapter
    }

    override fun clickListeners() {

    }

}