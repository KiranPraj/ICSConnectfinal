package org.icspl.icsconnect.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.ClipData
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.util.Log.i
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_raise_query.*
import kotlinx.android.synthetic.main.content_conversation.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.icspl.icsconnect.BuildConfig
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.ChatAdapter
import org.icspl.icsconnect.models.Chat
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import org.icspl.icsconnect.utils.FileManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class GroupChatActivity : AppCompatActivity(), ChatAdapter.DoccumentClickHandler {


    private val mLoginPreference by lazy { LoginPreference.getInstance(this@GroupChatActivity) }
    private val mDisposable = CompositeDisposable()
    private val mService by lazy { Common.getAPI() }
    private var mAdapter: ChatAdapter? = null
    private lateinit var photoPath: String
    private lateinit var queryId: String
    private var mToolbar: Toolbar? = null
    private lateinit var menu: Menu
    private var item: MenuItem? = null
    private lateinit var imageBtnFile: File
    private lateinit var imageToUploadUri: Uri
    private val DoccumentRequestCode: Int = 2112
    private val ImageRequestCode: Int = 2133
    private lateinit var fetch: Fetch
    private lateinit var request: Request
    private lateinit var progressBarMessage: TextView
    private lateinit var dialog: AlertDialog

    companion object {
        private val TAG = GroupChatActivity::class.java.simpleName
    }


    private var toemp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        initView()


    }


    private fun initView() {
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
            var strid=mLoginPreference.getStringData("id","")!!
            var members=mLoginPreference.getStringData("members","")!!
            if(members.contains(strid))
            {
                rl_bottom.visibility=View.GONE
            }
          if (intent != null) {
              photoPath = if (intent.getStringExtra("photo") != null) {
                  intent.getStringExtra("photo")
              } else {
                  ""
              }
              //queryId = intent.getStringExtra("queryId")
              toemp = intent.getStringExtra("toemp")
          }
        supportActionBar!!.title = "Query Id"

        bt_attachment.setOnClickListener { checkPermissions() }
        initRecyclerView()
        initDialog()
        fetchInit()

    }

    private fun initDialog() {
        val builder = AlertDialog.Builder(this@GroupChatActivity)
        val v = layoutInflater.inflate(R.layout.dialog_progress, null)  // this line
        progressBarMessage = v.findViewById(R.id.progress_bar_message)
        builder.setView(v)
        dialog = builder.create()
    }

    private fun initRecyclerView() {

        recyclerView.setHasFixedSize(true)
        val manager = androidx.recyclerview.widget.LinearLayoutManager(this@GroupChatActivity)
        manager.reverseLayout = false
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        mAdapter = ChatAdapter(getConversations(), this@GroupChatActivity, this@GroupChatActivity)

        recyclerView.setAdapter(mAdapter)
        recyclerView.postDelayed(
            {
               // recyclerView.smoothScrollToPosition(recyclerView.getAdapter()!!.getItemCount() - 1)
                mAdapter!!.notifyDataSetChanged()
            },
            1000
        )

        et_message.setOnClickListener({
            recyclerView.postDelayed({
                recyclerView.smoothScrollToPosition(
                    this.recyclerView.getAdapter()!!.getItemCount() - 1
                )
            }, 500)

        })
        bt_send.setOnClickListener({
            if (et_message.text.toString() != "") {

                sendMessages()
            }
            else{

                Toast.makeText(this,"error message",Toast.LENGTH_SHORT).show()

            }
        })


    }

    // Send Messages to Server
    private fun sendMessages() {

        var body: MultipartBody.Part? = null

        if (bt_attachment.tag != null) {
            val mFile = File(bt_attachment.tag.toString())
            val mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            body = MultipartBody.Part.createFormData("file", mFile.getName(), mRequestBody)
        } else {
            val file = RequestBody.create(MultipartBody.FORM, "")
            body = MultipartBody.Part.createFormData("file", "", file)
        }
        val data = ArrayList<Chat>()
        val item = Chat("2", et_message.text.toString(), Common().getTime(), photoPath, null)
        progress_chat.visibility = View.VISIBLE

        mDisposable.add(
            mService.sendGroupMessage(
                RequestBody.create(MediaType.parse("text/plain"), mLoginPreference.getStringData("groupId","")!!),
                RequestBody.create(MediaType.parse("text/plain"), et_message.text.toString()),
                body
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        if (s.get(0).response!! >= 1) {

                            Toast.makeText(
                                this@GroupChatActivity, "Query Sent Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            data.add(item)
                            mAdapter!!.addItem(data)
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter()!!.getItemCount() - 1)
                            et_message.setText("")
                            progress_chat.visibility = View.GONE
                        } else
                            Toast.makeText(
                                this@GroupChatActivity, "Query Failed to Send",
                                Toast.LENGTH_SHORT
                            ).show()
                        progress_chat.visibility = View.GONE
                    }
                }, { throwable ->
                    progress_chat.visibility = View.GONE
                    Log.i(TAG, throwable.message)
                })
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(org.icspl.icsconnect.R.menu.menu_open, menu)
        this.menu = menu
        item = menu.findItem(org.icspl.icsconnect.R.id.menu_close)
        item = menu.findItem(org.icspl.icsconnect.R.id.menu_close).setVisible(false)
        return true
    }

    // file picker dialog
    private fun checkPermissions() {
        //check Permission

        Dexter.withActivity(this@GroupChatActivity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    pickerDialog()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    permissionDenied()
                }

            }).check();

    }

    private fun permissionDenied() {
        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
            .with(ll_root_view_raised, "Camera and audio access is needed to take pictures of your dog")
            .withOpenSettingsButton("Settings")
            .withCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    sb!!.setText("Permission Required, Please Give Permission")
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
        val dialog = AlertDialog.Builder(this@GroupChatActivity)
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

    private fun getConversations(): ArrayList<Chat> {
        val data = ArrayList<Chat>()
        progress_chat.visibility = View.VISIBLE
        mDisposable.add(
            mService.GetGroupMessage(mLoginPreference.getStringData("groupId","")!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        progress_chat.visibility = View.GONE
                        s.groupMessages?.forEach {
                            val fromTo =
                                 if (mLoginPreference.getStringData("Group_id", "")!! == it.groupId) "1" else "1"

                            val item = Chat(fromTo,""+it.qMessage,""+it.qDate,""+it.qAttachment,"")
                            data.add(item)

                            progress_chat.visibility = View.GONE

                        }
                    } else
                        Log.i(TAG, "Null Data:")
                    progress_chat.visibility = View.GONE
                },
                    { throwable ->
                    run {
                        progress_chat.visibility = View.VISIBLE
                        Log.i(TAG, throwable.message)
                    }
                })
        )
        return data
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_close -> closeQuery()
        }
        return true
    }

    // close query button click handler
    private fun closeQuery() {
        //check for internet first
        if (!Common.isConnectedMobile(this@GroupChatActivity)) {
            Toast.makeText(this@GroupChatActivity, "Internet Required", Toast.LENGTH_LONG).show()
            return
        }

        AlertDialog.Builder(this@GroupChatActivity)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle("Close Query")
            .setMessage("Are you sure wants to Close the Query")
            .setPositiveButton(getString(R.string.close_query)) { dialog, which ->
                dialog.dismiss()
                var msg: String? = null
                progress_chat.visibility = View.VISIBLE
                mDisposable.add(
                    mService.closeQuery(queryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ s ->
                            s?.let {
                                if (it.get(0).response!! >= 1) {
                                    msg = "Query Closed Successfully"
                                    startActivity(Intent(this@GroupChatActivity, MainActivity::class.java))
                                    finish()
                                } else msg = "Query Failed to Close"
                                Toast.makeText(this@GroupChatActivity, msg, Toast.LENGTH_LONG).show()
                            }
                            progress_chat.visibility = View.GONE

                        }, { throwable ->
                            Log.i(TAG, throwable.message)
                            Toast.makeText(this@GroupChatActivity, "Error: ${throwable.message}", Toast.LENGTH_LONG)
                                .show()
                            progress_chat.visibility = View.GONE

                        })
                )

            }.setNegativeButton(android.R.string.cancel, { dialog, which ->
                dialog.dismiss()
            })
            .show()
    }

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
                "audio/x-wav|text/plain"
            )
            fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes)
            startActivityForResult(fileIntent, DoccumentRequestCode)
        } catch (e: Exception) {
            Toast.makeText(this@GroupChatActivity, e.message, Toast.LENGTH_SHORT).show()
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
            this@GroupChatActivity, BuildConfig.APPLICATION_ID + ".provider", imageBtnFile
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            cameraIntent.clipData = ClipData.newRawUri("", imageToUploadUri)
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(cameraIntent, ImageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ImageRequestCode -> {
                imageActivityResult()
            }
            DoccumentRequestCode -> {
                documentResultHandler(resultCode, data)
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun imageActivityResult() {
        run {

            i(RaiseQueryActivity.TAG, "onActivityResult: $imageToUploadUri")

            try {
                object : AsyncTask<Void, Void, String>() {
                    override fun doInBackground(vararg params: Void): String {

                        val path = compressImage(imageBtnFile.toString())

                        i(RaiseQueryActivity.TAG, "doInBackground: path: $path")

                        return path
                    }

                    override fun onPostExecute(path: String) {

                        try {
                            if (path != null) {
                                bt_attachment.apply {
                                    setBackgroundColor(
                                        ContextCompat.getColor(
                                            this@GroupChatActivity, android.R.color.holo_green_light
                                        )
                                    )
                                    tag = path
                                    Toast.makeText(
                                        this@GroupChatActivity, "File Attached Successfully", Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else
                                Toast.makeText(
                                    this@GroupChatActivity, "Failed to Attach File", Toast.LENGTH_SHORT
                                ).show()


                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@GroupChatActivity, "Failed to Attach File", Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                }.execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        fetch.close();
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

        val maxHeight = 816.0f
        val maxWidth = 612.0f
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
        i(RaiseQueryActivity.TAG, "getRealPathFromURI: $contentUri")


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
    private fun documentResultHandler(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {

            val selectedImage = data.data
            if (selectedImage != null) {
                val picturePath = FileManager.getPath(this@GroupChatActivity, selectedImage)
                Log.i(RaiseQueryActivity.TAG, "documentResultHandler: " + picturePath!!)


                println("picturePath +" + picturePath)  //path of sdcard
                bt_attachment.apply {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@GroupChatActivity, android.R.color.holo_green_light
                        )
                    )
                    tag = picturePath
                }
            }

        } else
            Toast.makeText(
                this@GroupChatActivity,
                getString(R.string.error_file_executong), Toast.LENGTH_LONG
            ).show()


    }

    private var urlDownload: String? = null

    // called by interface (Adapter)
    override fun ClickImageCallback(url: String?, v: View?) {
        enqueueDownload("https://upload.wikimedia.org/wikipedia/commons/1/16/HDRI_Sample_Scene_Balls_%28JPEG-HDR%29.jpg")
        urlDownload = "https://upload.wikimedia.org/wikipedia/commons/1/16/HDRI_Sample_Scene_Balls_%28JPEG-HDR%29.jpg"
    }

    //enquee download
    private fun enqueueDownload(url: String) {


        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/ICS Connect/fetch/" + Uri.parse(
                url
            ).getLastPathSegment()!!


        request = Request(url, dir)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL


        fetch.enqueue(request, Func {
            request = it
        }, Func {
            when (it) {
                Error.CONNECTION_TIMED_OUT -> {
                    enqueueDownload(urlDownload!!)
                }
                Error.NO_NETWORK_CONNECTION -> Toast.makeText(
                    this@GroupChatActivity, "No Connection",
                    Toast.LENGTH_LONG
                ).show()
                Error.UNKNOWN_IO_ERROR -> {
                    Toast.makeText(
                        this@GroupChatActivity, "Permission Required to Save Files in your Directory",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    Toast.makeText(this@GroupChatActivity, "Error:- $it", Toast.LENGTH_LONG).show()

                }
            }
            i(TAG, "Error: $it")
        })

        val mFetchListener = object : FetchListener {
            override fun onAdded(download: Download) {
                i(TAG, "onAdded")

            }

            override fun onCancelled(download: Download) {
                Toast.makeText(
                    this@GroupChatActivity, "Download Cancelled By User",
                    Toast.LENGTH_SHORT
                ).show()
                if (dialog.isShowing) dialog.dismiss()
            }

            override fun onCompleted(download: Download) {
                i(TAG, "onCompleted")
                Toast.makeText(this@GroupChatActivity, "Download Completes", Toast.LENGTH_SHORT).show()
                fetch.remove(download.id)
                if (dialog.isShowing) dialog.dismiss()

                val intent = Intent(Intent.ACTION_VIEW)
                val dirUri =
                    FileProvider.getUriForFile(
                        this@GroupChatActivity,
                        BuildConfig.APPLICATION_ID + ".provider", File(download.file)
                    );

                intent.setDataAndType(dirUri, "${contentResolver.getType(dirUri)}/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ActivityOptions.makeSceneTransitionAnimation(this@GroupChatActivity).toBundle()
                    startActivity(Intent.createChooser(intent, "Open With"), v)
                } else startActivity(intent)

            }

            override fun onDeleted(download: Download) {
            }

            override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                i(TAG, "onError Found $error")
                Toast.makeText(this@GroupChatActivity, "Error : $error", Toast.LENGTH_LONG).show()
            }

            override fun onPaused(download: Download) {
                i(TAG, "onPaused")
                Toast.makeText(this@GroupChatActivity, "Download Paused", Toast.LENGTH_LONG).show()

            }

            override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
                i(TAG, "onProgress")
                if (request.id == download.id) {
                    if (dialog.isShowing)
                        progressBarMessage.text =
                            download.progress.toString() + "% Downloading ${Common().getDownloadSpeedString(
                                this@GroupChatActivity,
                                downloadedBytesPerSecond
                            )}"

                }
            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
                i(TAG, "onQueued")

            }

            override fun onRemoved(download: Download) {
                i(TAG, "onRemoved")
                Toast.makeText(this@GroupChatActivity, "Download Removed", Toast.LENGTH_LONG).show()

                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }

            override fun onResumed(download: Download) {}

            override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
                i(TAG, "onStarted")
                Toast.makeText(this@GroupChatActivity, "Download Started", Toast.LENGTH_SHORT).show()
                if (!dialog.isShowing) {
                    dialog.show()
                }
            }

            override fun onWaitingNetwork(download: Download) {
                if (dialog.isShowing) progressBarMessage.text = getString(R.string.waiting_for_network)
            }
        }
        fetch.addListener(mFetchListener)
    }

    private fun fetchInit() {
        val fetchConfiguration = FetchConfiguration.Builder(this)
            .setDownloadConcurrentLimit(3)
            .enableRetryOnNetworkGain(true)
            .enableFileExistChecks(true)
            .enableLogging(true)
            .setNamespace("Downloading...")
            .build()

        fetch = Fetch.getInstance(fetchConfiguration)
    }
}
