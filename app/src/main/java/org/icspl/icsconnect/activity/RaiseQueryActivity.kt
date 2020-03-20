package org.icspl.icsconnect.activity


import android.Manifest
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.util.Log.i
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.mancj.materialsearchbar.MaterialSearchBar
import com.tonyodev.fetch2core.isNetworkAvailable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_raise_query.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.icspl.icsconnect.BuildConfig
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.SearchModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import org.icspl.icsconnect.utils.FileManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.LinkedHashMap


class RaiseQueryActivity : AppCompatActivity(),
    PopupMenu.OnMenuItemClickListener {

    val context: Context = this

    companion object {
        val TAG = RaiseQueryActivity::class.java.simpleName
    }

    private val mService by lazy { Common.getAPI() }
    private var searchList: MutableList<String> = ArrayList()
    private var searchHashList: LinkedHashMap<Int, SearchModel> = linkedMapOf()

    private lateinit var searchBar: MaterialSearchBar
    private val mDisposeOn = CompositeDisposable()
    private lateinit var imageBtnFile: File

    private lateinit var imageToUploadUri: Uri
    private val mLoginPreference by lazy { LoginPreference.getInstance(this@RaiseQueryActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.icspl.icsconnect.R.layout.activity_raise_query)
        progressrais.visibility=View.GONE
            if(isNetworkAvailable())
            {
                initSearchBar()

                btn_send_query.setOnClickListener { handleQuery() }


                btn_raised_attachment.setOnClickListener { checkPermissions() }
            }
            else
            {
                Toast.makeText(context,"No Internet Available",Toast.LENGTH_LONG).show()
            }
        //handleSearchChangeLisitenr()


    }

    // prepare data to send to server
    private fun handleQuery() {
        progressrais.visibility=View.VISIBLE
        if (et_message_query.text!!.isEmpty()) {
            Toast.makeText(this@RaiseQueryActivity, "Query cannot be empty", Toast.LENGTH_SHORT).show()
            til_raised.isErrorEnabled = true
            progressrais.visibility=View.GONE
            til_raised.error = "Query can not be Empty"
            return
        }
        var empname=tv_rased_to.text.toString()
        if(empname.equals(""))
        {
            progressrais.visibility=View.GONE
            Toast.makeText(context,"Please Select Employee Name",Toast.LENGTH_LONG).show()
            return
        }
        if (til_raised.isErrorEnabled)
            til_raised.isErrorEnabled = false

        var body: MultipartBody.Part? = null

        if (btn_raised_attachment.tag != null) {
            val mFile = File(btn_raised_attachment.tag.toString())
            val mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            body = MultipartBody.Part.createFormData("file", mFile.getName(), mRequestBody)
        } else {
            val file = RequestBody.create(MultipartBody.FORM, "")
            body = MultipartBody.Part.createFormData("file", "", file)
        }

        mDisposeOn.add(
            mService.postQuery(
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    mLoginPreference.getStringData("id", "")!!
                ), RequestBody.create(
                    MediaType.parse("text/plain"),
                    tv_rased_to.tag.toString()
                ), RequestBody.create(
                    MediaType.parse("text/plain"),
                    et_message_query.text.toString()
                ), RequestBody.create(
                    MediaType.parse("text/plain"),
                    Common().getDateFormat()
                ), RequestBody.create(
                    MediaType.parse("text/plain"),
                    mLoginPreference.getStringData("name", "")!!
                ), RequestBody.create(
                    MediaType.parse("text/plain"),
                   // searchHashList.get(0)!!.name.toString()
                    tv_rased_to.text.toString()
                ),
                body!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it != null) {
                        if (it[0].response!! >= 1) {

                            Toast.makeText(this@RaiseQueryActivity, "Query Raised Successfully", Toast.LENGTH_SHORT)
                                .show()
                            progressrais.visibility=View.GONE
                            startActivity(
                                Intent(
                                    this@RaiseQueryActivity, MainActivity
                                    ::class.java
                                )
                            )
                            this@RaiseQueryActivity.finish()
                        } else
                            Toast.makeText(this@RaiseQueryActivity, "Failed to Raised Query", Toast.LENGTH_SHORT)
                                .show()
                        progressrais.visibility=View.GONE
                    }
                })
    }

    private fun initSearchBar() {
        searchBar = findViewById(org.icspl.icsconnect.R.id.searchView)
        searchBar.setHint(getString(R.string.search_op_name))
        // searchBar.inflateMenu(org.icspl.icsconnect.R.menu.menu_open)
        searchBar.setCardViewElevation(10)
        searchBar.setMaxSuggestionCount(10)
        //searchList.add("Sanjay")
        //searchList.add("Parmesh")
        searchBar.lastSuggestions = searchList

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d("LOG_TAG", javaClass.simpleName + " text changed " + searchBar.text)

                val suggest = ArrayList<String>()
                for (search_term in searchList) {
                    if (search_term.toLowerCase().contentEquals(searchBar.text.toLowerCase())) {
                        suggest.add(search_term)
                        tv_rased_to.text = searchBar.text
                    }
                    searchBar.lastSuggestions = suggest

                }

                for (search_term in searchList) {
                    if (search_term.toLowerCase().contentEquals(searchBar.text.toLowerCase())) {
                        val v = searchHashList.getValue(searchList.indexOf(search_term))
                        tv_rased_to.text = v.name
                        tv_rased_to.tag = v.id
                    }
                    searchBar.lastSuggestions = suggest

                }
                setUpSearchObservable(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().isNotEmpty())
                    setUpSearchObservable(editable.toString())
            }
        })

        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onButtonClicked(buttonCode: Int) {}
            override fun onSearchStateChanged(enabled: Boolean) {}

            override fun onSearchConfirmed(text: CharSequence?) {
                i(TAG, "Search Confirmed")

                setUpSearchObservable(text.toString())
                searchBar.disableSearch()
            }
        })
        searchBar.disableSearch()
    }


/*private fun handleSearchChangeLisitenr() {
val searched = arrayOfNulls<String>(1)

searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(s: String): Boolean {

        return true
    }

    override fun onQueryTextChange(text: String): Boolean {
        searchList.clear()
        if (text.length >= 1)
            setUpSearchObservable(text)
        return true
    }
})

}*/

    private fun setUpSearchObservable(searched: String) {
        mService.search(searched)
            //     RxSearchObservable.fromView(searchView)
            .debounce(200, TimeUnit.MILLISECONDS)
            .filter(Predicate {
                if (it.isNotEmpty())
                    true
                else false
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<SearchModel>> {
                override fun accept(result: List<SearchModel>) {
                    searchList.clear()
                    searchHashList.clear()
                    if (!result.isNullOrEmpty()) {
                        for (model in result) {
                            searchHashList.put(result.indexOf(model), model)
                            i(TAG, "Key: ${result.indexOf(model)} : Value: ${model.name}")
                        }

                        for (model in searchHashList) {
                            searchList.add(model.value.name!! + " - " + model.value.id!!)
                        }
                        searchBar.lastSuggestions = searchList

                    }

                }
            })
    }


    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return true
    }

    override fun onStop() {
        mDisposeOn.clear()
        super.onStop()
    }

    // file picker dialog
    private fun checkPermissions() {
        //check Permission

        Dexter.withActivity(this@RaiseQueryActivity)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        pickerDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    permissionDenied()
                }

            }).check()
    }

    private fun howSettingDialog() {
        val builder = AlertDialog.Builder(this@RaiseQueryActivity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO Sertting") { dialog, _ ->
            run {
                dialog.dismiss()
                openSettings();
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> { dialog.cancel() } }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", getPackageName(), null)
        intent.setData(uri)
        startActivityForResult(intent, 101)
    }

    private fun permissionDenied() {
        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
            .with(ll_root_view_raised, "Camera and audio access is needed to take pictures of your dog")
            .withOpenSettingsButton("Settings")
            .withCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    sb!!.setText("Permission Required, Please Give Permission")
                    howSettingDialog()

                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    transientBottomBar!!.setText("Permission Needed Compulsory")

                }
            })
            .build();
    }

    private fun pickerDialog() {
        val view = layoutInflater.inflate(R.layout.griddialog_filechooser, null)
        val cameraLinearLayout = view.findViewById(R.id.ll_camera) as LinearLayout
        val docLinearLayout = view.findViewById(R.id.ll_doccument) as LinearLayout
        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .setPositiveButton(null, null)
            .setNegativeButton(null, null)

            .create()
        cameraLinearLayout.setOnClickListener {
            dialog.dismiss()
            pickImage()
        }
        docLinearLayout.setOnClickListener {
            dialog.dismiss()
            picDocuments()
        }

        dialog.show()
    }

    private val DOCCUMENT_REQUEST_CODE: Int = 2102
    private val IMAGE_REQUEST_CODE: Int = 2103

    private fun picDocuments() {

        try {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            //                        Uri uri = Uri.parse(Environment.getRootDirectory().getAbsolutePath());
            //                        fileIntent.setData(uri);
            fileIntent.type = "application/pdf"
            val extraMimeTypes = arrayOf(
                "audio/*",
                "video/*",
                "application/*",
                "application/pdf",
                "application/msword",
                "application/vnd.ms-powerpoint",
                "application/vnd.ms-excel",
                "application/zip",
                "audio/x-wav|text/plain",


                "application/msword",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
                "application/vnd.ms-word.document.macroEnabled.12",
                "application/vnd.ms-word.template.macroEnabled.12",
                "application/vnd.ms-excel",
                "application/vnd.ms-excel",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
                "application/vnd.ms-excel.sheet.macroEnabled.12",
                "application/vnd.ms-excel.template.macroEnabled.12",
                "application/vnd.ms-excel.addin.macroEnabled.12",
                "application/vnd.ms-excel.sheet.binary.macroEnabled.12",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/vnd.openxmlformats-officedocument.presentationml.template",
                "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
                "application/vnd.ms-powerpoint.addin.macroEnabled.12",
                "application/vnd.ms-powerpoint.presentation.macroEnabled.12",
                "application/vnd.ms-powerpoint.template.macroEnabled.12",
                "application/vnd.ms-powerpoint.slideshow.macroEnabled.12",
                "application/vnd.ms-access"
            )
            fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes)
            startActivityForResult(fileIntent, DOCCUMENT_REQUEST_CODE)
        } catch (e: Exception) {

            Toast.makeText(this@RaiseQueryActivity,"Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun pickImage() {

        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/ICS Connect/Images/"
        val newdir = File(dir)
        newdir.mkdirs()
        val file = dir + "Connect_" + DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString() + ".jpg"

        imageBtnFile = File(file)
        try {
            imageBtnFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        imageToUploadUri = FileProvider.getUriForFile(
            this@RaiseQueryActivity, BuildConfig.APPLICATION_ID + ".provider", imageBtnFile
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            cameraIntent.clipData = ClipData.newRawUri("", imageToUploadUri)
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(cameraIntent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            IMAGE_REQUEST_CODE -> {
                imageActivityResult()
            }
            DOCCUMENT_REQUEST_CODE -> {
                documentResultHandler(resultCode, data, btn_raised_attachment)
            }
        }
    }

    private fun imageActivityResult() {
        run {

            i(TAG, "onActivityResult: $imageToUploadUri")

            try {
                object : AsyncTask<Void, Void, String>() {
                    override fun doInBackground(vararg params: Void): String {

                        val path = compressImage(imageBtnFile.toString())

                        i(TAG, "doInBackground: path: $path")

                        return path
                    }

                    override fun onPostExecute(path: String) {

                        try {
                            if (path != null) {
                                btn_raised_attachment.apply {
                                    text = "Attached"
                                    setBackgroundColor(
                                        ContextCompat.getColor(
                                            this@RaiseQueryActivity, android.R.color.holo_green_light
                                        )
                                    )
                                    tag = path
                                    Toast.makeText(
                                        this@RaiseQueryActivity, "File Attached Successfully", Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else
                                Toast.makeText(
                                    this@RaiseQueryActivity, "Failed to Attach File", Toast.LENGTH_SHORT
                                ).show()


                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@RaiseQueryActivity, "Failed to Attach File", Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                }.execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    // compress images
    fun compressImage(imageUri: String): String {

        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image is taken as 816x612

        val maxHeight = 1500.0f
        val maxWidth = 2000.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename
    }

    fun getFilename(): String {
        val file = File(Environment.getExternalStorageDirectory().path, "ICS Connect/")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + "Connect_" + UUID.randomUUID().toString() + "_IMG_" + System.currentTimeMillis() + ".jpg"
    }

    private fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        i(TAG, "getRealPathFromURI: $contentUri")


        val cursor = getContentResolver().query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            cursor!!.moveToFirst()
            val index = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            //cursor.close();
            return cursor!!.getString(index)
        }
    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    /**
     * Handle File Doc image button clicked Result
     *
     * @param resultCode : onActivityResult resultcode
     * @param data       : onActivityResult intent data
     * @param fileButton : Button where to setting tag, changing background, text
     */
    private fun documentResultHandler(resultCode: Int, data: Intent?, fileButton: Button) {
        if (resultCode == RESULT_OK && data != null) {

            val selectedImage = data.data
            if (selectedImage != null) {
                val picturePath = FileManager.getPath(this@RaiseQueryActivity, selectedImage)
                Log.i(TAG, "documentResultHandler: " + picturePath!!)
                try {
                    val file = File(picturePath)
                    if (file.exists()) {
                        if (file.length() / 1024 >= 5120) { //15 mb
                            Toast.makeText(this@RaiseQueryActivity, "File Size limit is upto 5MB", Toast.LENGTH_LONG)
                                .show()
                            return
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                println("picturePath +" + picturePath)  //path of sdcard
                btn_raised_attachment.apply {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@RaiseQueryActivity, android.R.color.holo_green_light
                        )
                    )
                    tag = picturePath
                    text = this@RaiseQueryActivity.getString(R.string.attached)
                }
            }

        } else
            Toast.makeText(
                this@RaiseQueryActivity,
                getString(R.string.error_file_executong), Toast.LENGTH_LONG
            ).show()


    }
}
