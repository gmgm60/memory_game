package com.gmgm60.memorygame

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import models.BoardSize
import kotlin.math.min

class ImagePickerAdapter(
    private val context: Context,
    private val selectedImages: List<Uri>,
    private val boardSize: BoardSize,
    private val landscape: Boolean,
) : RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {
    companion object {
        private const val MARGIN_SIZE = 8
        private const val TAG = "ImagePickerAdapter"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val boardSizeWidth = if (landscape) boardSize.height else boardSize.width
        val boardSizeHeight = if (landscape) boardSize.width else boardSize.height
        val width = parent.width / boardSizeWidth
        val height = parent.height / boardSizeHeight
        val cardLength = min(width, height) - (MARGIN_SIZE * 2)
        val view = LayoutInflater.from(context).inflate(R.layout.card_image, parent, false)
        val layoutParams = view.findViewById<ImageView>(R.id.ivCustomImage).layoutParams
        layoutParams.height = cardLength
        layoutParams.width = cardLength
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = boardSize.pairsCount

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (selectedImages.size > position) {
            holder.bind(selectedImages[position])
        } else {
            holder.bind()
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCustomImage = itemView.findViewById<ImageView>(R.id.ivCustomImage)

        fun bind() {
            ivCustomImage.setOnClickListener({
                //todo:
            })
        }

        fun bind(uri: Uri) {
            ivCustomImage.setImageURI(uri)
            ivCustomImage.setOnClickListener(null)
        }

    }

}
