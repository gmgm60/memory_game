package com.gmgm60.memorygame

import android.animation.ArgbEvaluator
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import models.BoardSize
import models.MemoryGame


class MainActivity : AppCompatActivity() {


    private var landscape: Boolean = false;
    private lateinit var clRoot: ConstraintLayout;
    private lateinit var rvBoard: RecyclerView;
    private lateinit var tvNumMoves: TextView;
    private lateinit var tvNumPairs: TextView;
    private var boardSize = BoardSize.MEDIUM

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // The Toolbar defined in the layout has the id "my_toolbar".
        setSupportActionBar(findViewById(R.id.my_toolbar))

        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoeard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)


        setupBoard()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_refresh -> {
                showAlertDialog(
                    "Are you Sure ?",
                    null,
                ) {
                    setupBoard()

                }
                return true
            }

            R.id.mi_new_size -> {
                val boardSizeView =
                    LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
                val radioGroup = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
                showAlertDialog(
                    "Are you Sure ?",
                    boardSizeView,
                ) {

                    if (radioGroup.checkedRadioButtonId != -1) {
                        when (radioGroup.checkedRadioButtonId) {
                            R.id.rbEasy -> {
                                boardSize = BoardSize.EASY
                            }

                            R.id.rbMedium -> {
                                boardSize = BoardSize.MEDIUM
                            }

                            R.id.rbHard -> {
                                boardSize = BoardSize.HARD
                            }
                        }
                        setupBoard()

                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        landscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        setBoardView()
        super.onConfigurationChanged(newConfig)

    }

    private fun showAlertDialog(title: String, view: View?, onClickListener: OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Ok") { _, _ -> onClickListener.onClick(null) }
            .show()
    }

    private fun setupBoard() {
        memoryGame = MemoryGame(boardSize)
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        tvNumMoves.text =
            buildString {
                append(getString(R.string.numMoves))
                append(' ')
                append("${memoryGame.numMoves}".padStart(2, '0'))
            }
        tvNumPairs.text =
            buildString {
                append(getString(R.string.pairs_count))
                append(' ')
                append("${memoryGame.numPairsFound}".padStart(2, '0'))
                append("/")
                append("${boardSize.pairsCount}".padStart(2, '0'))
            }

        rvBoard.setHasFixedSize(true)

        setBoardView()
    }

    private fun setBoardView() {
        rvBoard.layoutManager =
            GridLayoutManager(this, if (landscape) boardSize.height else boardSize.width)
        adapter = MemoryBoardAdapter(
            this,
            boardSize,
            memoryGame.cards,
            landscape,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    onClick(position)
                }

            })
        rvBoard.adapter = adapter
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
                append(' ')
                append("${memoryGame.numPairsFound}".padStart(2, '0'))
                append("/")
                append("${boardSize.pairsCount}".padStart(2, '0'))
            }
        val color = ArgbEvaluator().evaluate(
            memoryGame.numPairsFound.toFloat() / boardSize.pairsCount,
            ContextCompat.getColor(this, R.color.color_progress_none),
            ContextCompat.getColor(this, R.color.color_progress_full)
        ) as Int
        tvNumPairs.setTextColor(color)

        tvNumMoves.text = buildString {
            append(getString(R.string.numMoves))
            append(' ')
            append(memoryGame.numMoves.toString().padStart(2, '0'))
        }

        if (memoryGame.isWin) {
            Snackbar.make(clRoot, "You Won! Congratulation", Snackbar.LENGTH_LONG).show()
            return
        }
    }

}