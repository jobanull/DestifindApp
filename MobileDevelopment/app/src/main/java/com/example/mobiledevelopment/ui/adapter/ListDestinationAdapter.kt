package com.example.mobiledevelopment.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobiledevelopment.data.response.ListDestinationItem
import com.example.mobiledevelopment.databinding.DestinationItemBinding
import com.example.mobiledevelopment.ui.detail.DetailActivity

class ListDestinationAdapter : ListAdapter<ListDestinationItem, ListDestinationAdapter.MyViewHolder>(DIFF_CALLBACK) {



    class MyViewHolder(binding: DestinationItemBinding) : RecyclerView.ViewHolder(binding.root){
        private val title = binding.title
        private val image = binding.images
        private val desc =binding.description
        fun bind(review: ListDestinationItem){
            title.text = review.name
            desc.text = review.description
            Glide.with(itemView.context)
                .load(review.photoUrl)
                .into(image)
            itemView.setOnClickListener{
                val intentDetail  = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(KEY, review)

                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(image, "profile"),
                        Pair(title,"name"),
                        Pair(desc, "description")
                    )
                itemView.context.startActivity(intentDetail, optionCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = DestinationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    companion object {
        const val KEY = "key_story"
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ListDestinationItem>(){
            override fun areItemsTheSame(oldItem: ListDestinationItem, newItem: ListDestinationItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListDestinationItem,
                newItem: ListDestinationItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}