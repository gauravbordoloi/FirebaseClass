package com.codercampy.firebaseclass.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codercampy.firebaseclass.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val users: ArrayList<UserModel> = ArrayList()

    fun setUsers(users: List<UserModel>) {
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position], position)
    }


}

class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userModel: UserModel, position: Int) {
        binding.tvPhone.text = userModel.phone
        binding.tvName.text = userModel.name
        Glide.with(binding.ivImage).load(userModel.photo).into(binding.ivImage)
    }

}