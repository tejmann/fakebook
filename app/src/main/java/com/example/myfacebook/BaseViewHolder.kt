package com.example.myfacebook

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.create_post.view.*
import kotlinx.android.synthetic.main.empty_view_holder.view.*
import kotlinx.android.synthetic.main.image_view_holder.view.*
import kotlinx.android.synthetic.main.profile_view_holder.view.*
import kotlinx.android.synthetic.main.status_view_holder.view.*
import kotlinx.android.synthetic.main.users_view_holder.view.*

abstract class BaseViewHolder<T : Post>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bindView(post: T)
    abstract fun recycleView()
}

class StatusViewHolder(itemView: View, private val clickListener: ClickListener) :
    BaseViewHolder<Status>(itemView) {
    override fun bindView(post: Status) = with(itemView) {
        text_view_holder.text = post.data.message
        user_info_name_status.text = post.data.name.capitalize()
        user_info_time_status.text = post.data.time.toDate().toLocaleString()
        if (post.data.dpPath != "undefined") {
            val storageReference = Firebase.storage.getReference(post.data.dpPath)
            Glide.with(context /* context */)
                .load(storageReference)
                .circleCrop()
                .into(user_info_dp_status)
            return@with
        }

    }

    override fun recycleView() = with(itemView) {
        text_view_holder.text = null
    }

}

class ImageViewHolder(itemView: View, private val clickListener: ClickListener) :
    BaseViewHolder<Image>(itemView) {
    override fun bindView(post: Image) = with(itemView) {
        user_info_name_image.text = post.data.name.capitalize()
        user_info_time_image.text = post.data.time.toDate().toLocaleString()
        val storageReference = Firebase.storage.getReference(post.data.filePath)
        Glide.with(context /* context */)
            .load(storageReference)
            .into(image)
        if (post.data.dpPath != "undefined") {
            val dpReference = Firebase.storage.getReference(post.data.dpPath)
            Glide.with(context /* context */)
                .load(dpReference)
                .circleCrop()
                .into(user_info_dp_image)
            return@with
        }
    }

    override fun recycleView() = with(itemView) {
        image.setImageDrawable(null)
        user_info_name_image.text = null
        user_info_time_image.text = null
        user_info_dp_image.setImageDrawable(null)
    }

}

class CreatePostViewHolder(itemView: View, private val clickListener: ClickListener) :
    BaseViewHolder<Create>(itemView) {
    override fun bindView(post: Create) = with(itemView) {
        photo_container.setOnClickListener {
            clickListener.onItemClick(post, CreateExtras(CreatePostActions.IMAGE))
        }
        status_button.setOnClickListener {
            clickListener.onItemClick(post, CreateExtras(CreatePostActions.STATUS))
        }
        if (post.data.filePath != "undefined") {
            val storageReference = Firebase.storage.getReference(post.data.filePath)
            Glide.with(context /* context */)
                .load(storageReference)
                .circleCrop()
                .into(display_picture)
            return@with
        }
    }

    override fun recycleView() = with(itemView) {
        status_button.setOnClickListener(null)
        display_picture.setImageDrawable(null)
        photo_container.setOnClickListener(null)
    }
}

class UsersViewHolder(itemView: View, private val clickListener: ClickListener) :
    BaseViewHolder<Users>(itemView) {

    override fun recycleView() = with(itemView) {
        user_dp.setImageDrawable(null)
        user_name.text = null
        user_name.setOnClickListener(null)
    }

    override fun bindView(post: Users) = with(itemView) {
        user_name.text = post.data.name.capitalize()
        user_name.setOnClickListener {
            clickListener.onItemClick(post, null)
        }
        if (post.data.filePath != "undefined") {
            val storageReference = Firebase.storage.getReference(post.data.filePath)
            Glide.with(context /* context */)
                .load(storageReference)
                .circleCrop()
                .into(user_dp)
            return@with
        }
    }
}

class ProgressViewHolder(itemView: View, private val clickListener: ClickListener) :
    BaseViewHolder<Progress>(itemView) {

    override fun recycleView() = with(itemView) {
    }

    override fun bindView(post: Progress) = with(itemView) {
    }
}

class EmptyViewHolder(itemView: View, private val clickListener: ClickListener) :
    BaseViewHolder<Empty>(itemView) {

    override fun recycleView() = with(itemView) {
    }

    override fun bindView(post: Empty) = with(itemView) {
        empty_text.text = when (post.data.type) {
            EmptyDataType.WALL -> context.getString(R.string.empty_wall)
            EmptyDataType.PROFILE -> context.getString(R.string.empty_profile)
            EmptyDataType.USERS -> context.getString(R.string.empty_users)
        }
    }
}

class ProfileViewHolder(itemView: View, private val clickListener: ClickListener) :
    BaseViewHolder<Profile>(itemView) {

    override fun recycleView() = with(itemView) {
        user_profile_name.text = null
        add_friend.text = null
        add_friend.setOnClickListener(null)
        user_profile_dp.setImageDrawable(null)
    }

    override fun bindView(post: Profile) = with(itemView) {
        user_profile_name.text = post.data.name.capitalize()
        val myUid = Firebase.auth.uid
        add_friend.isEnabled = (!post.data.following)
        add_friend.text = when {
            post.data.following -> context.getString(R.string.user_followed)
            post.data.uid == myUid -> context.getString(R.string.sign_out)
            else -> context.getString(R.string.follow)
        }

        add_friend.setOnClickListener {
            val extras = if (post.data.uid == myUid) ProfileExtras(ProfileActions.SIGNOUT)
            else ProfileExtras(ProfileActions.FOLLOW)

            clickListener.onItemClick(post, extras)
            add_friend.text = if (post.data.uid == myUid) context.getString(R.string.sign_out)
            else context.getString(R.string.user_followed)
            add_friend.isEnabled = false
        }

        if (post.data.filePath != "undefined") {
            val storageReference = Firebase.storage.getReference(post.data.filePath)
            Glide.with(context /* context */)
                .load(storageReference)
                .circleCrop()
                .into(user_profile_dp)
            return@with
        }
    }
}