package com.dicoding.myprojectandroidfundamental.ui.insert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myprojectandroidfundamental.database.FavoriteUser
import com.dicoding.myprojectandroidfundamental.databinding.ItemUserBinding
import com.dicoding.myprojectandroidfundamental.helper.FavoriteUsersDiffCallback

class FavoriteUsersAdapter : RecyclerView.Adapter<FavoriteUsersAdapter.FavoriteUsersViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback


    private val listFavoriteUsers = ArrayList<FavoriteUser>()
    fun setListUsersFavorite(listFavoriteUsers: List<FavoriteUser>) {
        val diffCallback = FavoriteUsersDiffCallback(this.listFavoriteUsers, listFavoriteUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUsers.clear()
        this.listFavoriteUsers.addAll(listFavoriteUsers)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUsersViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFavoriteUsers.size
    }

    override fun onBindViewHolder(holder: FavoriteUsersViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavoriteUsers[position])
        }
    }

    class FavoriteUsersViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(usersFavEntity: FavoriteUser) {
            with(binding) {
                tvNameUsers.text = usersFavEntity.username
                Glide.with(root)
                    .load(usersFavEntity.avatar)
                    .into(ivImageUsers)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }
}