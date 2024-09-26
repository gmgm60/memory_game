package com.gmgm60.memorygame

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import models.BoardSize
import utils.EXTRA_BOARD_SIZE
import utils.isPermissionGranted
import utils.requestPermission
import kotlin.math.min

class CreateActivity : AppCompatActivity() {
    private lateinit var boardSize: BoardSize
    private lateinit var rvImagePicker: RecyclerView
    private lateinit var etGameName: EditText
    private lateinit var btSave: Button
    private lateinit var adapter: ImagePickerAdapter

    private var numRequiredImages = -1
    private var selectedImages = mutableListOf<Uri>()


    companion object {
        private const val TAG = "CreateActivity"
        private const val PICK_REQUEST_CODE = 1988
        private const val READ_PHOTO_REQUEST_CODE = 8819
        private val READ_PHOTO_PERMISSION =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    android.Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                }
            }
    }

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

        boardSize = intent.getStringExtra(EXTRA_BOARD_SIZE)!!.let {
            BoardSize.valueOf(it)
        }


        numRequiredImages = boardSize.pairsCount
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chose Pics: (${selectedImages.size} / $numRequiredImages)"



        adapter = ImagePickerAdapter(this, selectedImages, boardSize, false) {
            if (isPermissionGranted(this, READ_PHOTO_PERMISSION)) {
                lunchIntentForPhotos()
            } else {
                requestPermission(this, READ_PHOTO_PERMISSION, READ_PHOTO_REQUEST_CODE)
            }
        }
        rvImagePicker.adapter = adapter

        rvImagePicker.setHasFixedSize(true)
        rvImagePicker.layoutManager = GridLayoutManager(this, boardSize.width)
    }

    private fun lunchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Choose Pics"), PICK_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        isPermissionGranted(requestCode, READ_PHOTO_REQUEST_CODE, grantResults) { isGranted ->
            if (isGranted) {
                lunchIntentForPhotos()
            } else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != PICK_REQUEST_CODE || resultCode != Activity.RESULT_OK || data == null) {
            Log.i(TAG, "No Data")
        } else {
            val selectedUri = data.data
            val clipData = data.clipData
            if (clipData != null) {
                val remaining = numRequiredImages - selectedImages.size
                for (i in 0 until min(remaining, clipData.itemCount)) {
                    selectedImages.add(clipData.getItemAt(i).uri)
                    adapter.notifyItemChanged(selectedImages.size - 1)
                }
            } else if (selectedUri != null) {
                selectedImages.add(selectedUri)
                adapter.notifyItemChanged(selectedImages.size - 1)
            }
            supportActionBar?.title = "Chose Pics: (${selectedImages.size} / $numRequiredImages)"
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}