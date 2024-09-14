package com.gmgm60.memorygame

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import models.BoardSize
import models.MemoryGame


class MainActivity : AppCompatActivity() {


    private lateinit var clRoot: ConstraintLayout;
    private lateinit var rvBoard: RecyclerView;
    private lateinit var tvNumMoves: TextView;
    private lateinit var tvNumPairs: TextView;
    private val boardSize = BoardSize.EASY

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoeard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        memoryGame = MemoryGame(boardSize)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.width)
        rvBoard.setHasFixedSize(true)
        adapter = MemoryBoardAdapter(
            this,
            boardSize,
            memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    onClick(position)
                }

            })
        rvBoard.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onClick(position: Int) {
        val flipError: String? = memoryGame.canFlip(position)
        if (flipError != null) {
            Snackbar.make(clRoot, flipError, Snackbar.LENGTH_LONG).show()
            return
        }
        val ids = memoryGame.flipCard(position)
        ids.forEach { id ->
            adapter.notifyItemChanged(id)
        }
        tvNumPairs.text =
            buildString {
                append(getString(R.string.pairs_count))
                append("${memoryGame.numPairsFound}".padStart(2, '0'))
                append("/")
                append("${boardSize.pairsCount}".padStart(2, '0'))
            }
        tvNumMoves.text = buildString {
            append(getString(R.string.numMoves))
            append(memoryGame.numMoves.toString())
        }

        if (memoryGame.isWin) {
            Snackbar.make(clRoot, "You Won! Congratulation", Snackbar.LENGTH_LONG).show()
            return
        }
    }
}