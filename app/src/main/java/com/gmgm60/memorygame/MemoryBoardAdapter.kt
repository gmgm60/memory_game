package com.gmgm60.memorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import models.BoardSize
import models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val landscape: Boolean,
    private val cardClickListener: CardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object {
        private const val MARGIN_SIZE = 2
        private const val TAG = "MemoryBoardAdapter"
    }

    interface CardClickListener {
        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val boardSizeWidth = if (landscape)boardSize.height  else boardSize.width
        val boardSizeHeight = if (landscape) boardSize.width else boardSize.height
        val width = parent.width /boardSizeWidth
        val height = parent.height / boardSizeHeight
        val cardLength = min(width, height) - (MARGIN_SIZE * 2)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val params = view.findViewById<CardView>(R.id.cardView).layoutParams as MarginLayoutParams
        params.height = cardLength;
        params.width = cardLength;
        params.setMargins(MARGIN_SIZE)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = boardSize.count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position: Int) {
            val card = cards[position]
            val id =
                if (card.isFaceUp)
                    card.identifier
                else
                    R.drawable.ic_launcher_background

            imageButton.setImageResource(id)
            imageButton.alpha = if (card.isMatched) 0.4f else 1f
            val colorStateList = if (card.isMatched) ContextCompat.getColorStateList(
                context,
                R.color.color_gray
            ) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            imageButton.setOnClickListener {
                Log.i(TAG, "Clicked on position $position")
                cardClickListener.onCardClicked(position)
            }
        }
    }

}
