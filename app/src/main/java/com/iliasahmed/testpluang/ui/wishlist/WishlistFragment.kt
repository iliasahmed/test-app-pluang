package com.iliasahmed.testpluang.ui.wishlist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.iliasahmed.testpluang.R
import com.iliasahmed.testpluang.databinding.FragmentWishlistBinding
import com.iliasahmed.testpluang.model.QuotesModel
import com.iliasahmed.testpluang.ui.base.BaseFragment
import com.iliasahmed.testpluang.utils.ViewDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class WishlistFragment :  BaseFragment<FragmentWishlistBinding, WishListViewModel>(
    WishListViewModel::class.java,
    R.layout.fragment_wishlist
) {
    private var wishLists = ArrayList<QuotesModel>()
    private var adapter: WishListAdapter? = null
    private var manager: LinearLayoutManager? = null
    private var alert: ViewDialog? = null

    override fun initViews() {
        alert = ViewDialog(activity)
        alert!!.showDialog()
        viewModel.loadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun liveEventsObservers() {
        viewModel.liveList.observe(viewLifecycleOwner) { wishListEntities ->
            wishLists.clear()
            wishLists.addAll(wishListEntities)
            adapter!!.notifyDataSetChanged()
            alert!!.hideDialog()
            if (wishListEntities.isNotEmpty()) {
                binding.empty.visibility = View.GONE
            } else {
                binding.empty.visibility = View.VISIBLE
            }
        }

        viewModel.deletedObject.observe(viewLifecycleOwner){
            Log.e(TAG, "************")
            Toast.makeText(context, "$it: deleted", Toast.LENGTH_SHORT).show()
            alert!!.showDialog()
            viewModel.loadData()
        }
    }

    override fun setupRecycler() {
        manager = LinearLayoutManager(context)
        binding.recycle.layoutManager = manager
        adapter = WishListAdapter(wishLists, context, object :
            WishListAdapter.WishListListener {
            override fun checkEmpty() {}
            override fun delete(sid: String) {
                viewModel.deleteItemFromWishlist(sid)
            }
        })
        binding.recycle.adapter = adapter
    }

    override fun clickListeners() {

    }
}