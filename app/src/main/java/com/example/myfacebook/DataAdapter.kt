package com.example.myfacebook

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(
    private val viewHolderFactory: ViewHolderFactory<Post>,
    val clickListener: ClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<Post>>() {
    var data: List<Post> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private fun getPostType(ordinal: Int) = PostType.values()[ordinal]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Post> =
        viewHolderFactory.getViewHolder(getPostType(viewType), parent, clickListener)

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].otype.ordinal

    override fun onBindViewHolder(holder: BaseViewHolder<Post>, position: Int) {
        holder.bindView(data[position])
    }

    override fun onViewRecycled(holder: BaseViewHolder<Post>) {
        super.onViewRecycled(holder)
        holder.recycleView()
    }
}

interface ClickListener {
    fun onItemClick(post: Post, extras: BaseExtras? = null)
}

sealed class BaseExtras
data class CreateExtras(val action: CreatePostActions) : BaseExtras()
data class ProfileExtras(val action: ProfileActions): BaseExtras()
enum class CreatePostActions {
    IMAGE,
    STATUS,
    OTHER
}

enum class ProfileActions {
    FOLLOW,
    SIGNOUT
}