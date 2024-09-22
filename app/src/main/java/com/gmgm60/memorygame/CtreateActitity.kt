package com.gmgm60.memorygame

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import models.BoardSize
import utils.EXTRA_BOARD_SIZE

class CreateActivity : AppCompatActivity() {
    private lateinit var boardSize: BoardSize;
    private lateinit var rvImagePicker: RecyclerView;
    private lateinit var etGameName: EditText;
    private lateinit var btSave: Button;

    private var numRequiredImages = -1
    private var selectedImages = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)

        setSupportActionBar(findViewById(R.id.my_toolbar2))

        rvImagePicker = findViewById(R.id.rvImagePicker)
        etGameName = findViewById(R.id.etGameName)
        btSave = findViewById(R.id.btSave)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE, BoardSize::class.java)!!
        }
        numRequiredImages = boardSize.pairsCount
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "numRequiredImages: (0 / $numRequiredImages)"


        rvImagePicker.adapter = ImagePickerAdapter(this,selectedImages,boardSize,false)
        rvImagePicker.setHasFixedSize(true)
        rvImagePicker.layoutManager = GridLayoutManager(this, boardSize.width)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}