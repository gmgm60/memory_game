package com.gmgm60.memorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import models.BoardSize
import models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    interface CardClickListener {
        fun onCardClicked(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position: Int) {
            val id =
                if (cards[position].isFaceUp)
                    cards[position].identifier
                else
                    R.drawable.ic_launcher_background

            imageButton.setImageResource(id)
            imageButton.setOnClickListener {
                Log.i(TAG, "Clicked on position $position")
                cardClickListener.onCardClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val width = parent.width / boardSize.width
        val height = parent.height / boardSize.height
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


}
