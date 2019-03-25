package org.icspl.icsconnect.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.database.Cursor
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_raise_query.*
import kotlinx.android.synthetic.main.content_conversation.*
import kotlinx.android.synthetic.main.fragment_grpnamefragment_list.*
import kotlinx.android.synthetic.main.fragment_grpnamefragment_list.view.*
import kotlinx.android.synthetic.main.row_group_name.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.icspl.icsconnect.BuildConfig
import org.icspl.icsconnect.R
import org.icspl.icsconnect.activity.ChatActivity
import org.icspl.icsconnect.activity.GroupActivity
import org.icspl.icsconnect.activity.GroupChatActivity
import org.icspl.icsconnect.activity.RaiseQueryActivity
import org.icspl.icsconnect.adapters.GroupNameAdapter
import org.icspl.icsconnect.models.Chat
import org.icspl.icsconnect.models.GroupChat
import org.icspl.icsconnect.models.IndividualGrpNameModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import org.icspl.icsconnect.utils.FileManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class GrpNameFragmentFragment : androidx.fragment.app.Fragment(), GroupNameAdapter.GrpNameCallback {

    private val mLoginPreference by lazy { LoginPreference.getInstance(requireContext()) }
    private val mService by lazy { Common.getAPI() }
    private val mDisposable = CompositeDisposable()
    private lateinit var mView: View
    private var photoPath: String?=null
    private lateinit var mAdapter: GroupNameAdapter
    private var mNameList: MutableList<IndividualGrpNameModel.IndividualGroup>?=null
    private var ar=ArrayList<String>()
    private lateinit var imageBtnFile: File
    private lateinit var imageToUploadUri: Uri
    private val DoccumentRequestCode: Int = 2112
    private val ImageRequestCode: Int = 2133
  // public lateinit var  mLoginPreference: LoginPreference
 //   private lateinit var c

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        mView = inflater.inflate(R.layout.fragment_grpnamefragment_list, container, false)

        initViews()



        mView.rl_bottom.visibility=View.GONE
        mView.bt_attachment.setOnClickListener { checkPermissions() }
        mView.bt_send.setOnClickListener(View.OnClickListener {
            if (mView.et_message.getText().toString() != "") {
                sendMessages()
            }

        })
        return mView
    }

    private fun checkPermissions() {
        //check Permission

        Dexter.withActivity(activity)
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
        val dialog = AlertDialog.Builder(this!!.context!!)
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
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
            this!!.activity!!, BuildConfig.APPLICATION_ID + ".provider", imageBtnFile
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            cameraIntent.clipData = ClipData.newRawUri("", imageToUploadUri)
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(cameraIntent, ImageRequestCode)
    }


    private fun sendMessages() {

        var body: MultipartBody.Part? = null

        if (mView.bt_attachment.tag != null) {

            val mFile = File(mView.bt_attachment.tag.toString())
            val mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            body = MultipartBody.Part.createFormData("file", mFile.getName(), mRequestBody)

        } else {
            val file = RequestBody.create(MultipartBody.FORM, "")
            body = MultipartBody.Part.createFormData("file", "", file)
        }
        val data = java.util.ArrayList<GroupChat>()
        val item = GroupChat(ar, mView.et_message.getText().toString(), Common().getTime(), photoPath.toString(), null)
//        progress_chat.visibility = View.VISIBLE

        mDisposable.add(
            mService.sendMasterGroupMessage(
                ar,
                RequestBody.create(MediaType.parse("text/plain"), mView.et_message.text.toString()),
                   body
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        if (s.get(0).response!! >= 1) {

                            Toast.makeText(
                                context, "Query Sent Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            data.add(item)
                            mView.et_message.setText("")
                            startActivity(Intent(context,GroupActivity::class.java))
                            //progress_chat.visibility = View.GONE
                        } else
                            Toast.makeText(
                                context, "Query Failed to Send",
                                Toast.LENGTH_SHORT
                            ).show()
                      //  progress_chat.visibility = View.GONE
                    }
                }, { throwable ->
              //      progress_chat.visibility = View.GONE
                })
        )
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

            Log.i(RaiseQueryActivity.TAG, "onActivityResult: $imageToUploadUri")

            try {
                object : AsyncTask<Void, Void, String>() {
                    override fun doInBackground(vararg params: Void): String {

                        val path = compressImage(imageBtnFile.toString())

                        Log.i(RaiseQueryActivity.TAG, "doInBackground: path: $path")

                        return path
                    }

                    override fun onPostExecute(path: String) {

                        try {
                            if (path != null) {
                                mView.bt_attachment.apply {
                                    setBackgroundColor(
                                        ContextCompat.getColor(
                                            context, android.R.color.holo_green_light
                                        )
                                    )
                                    tag = path
                                    Toast.makeText(
                                        context, "File Attached Successfully", Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else
                                Toast.makeText(
                                    context, "Failed to Attach File", Toast.LENGTH_SHORT
                                ).show()


                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                context, "Failed to Attach File", Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                }.execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }
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
    private fun documentResultHandler(resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {

            val selectedImage = data.data
            if (selectedImage != null) {
                val picturePath = FileManager.getPath(context, selectedImage)
                Log.i(RaiseQueryActivity.TAG, "documentResultHandler: " + picturePath!!)


                println("picturePath +" + picturePath)  //path of sdcard
                mView.bt_attachment.apply {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context, android.R.color.holo_green_light
                        )
                    )
                    tag = picturePath
                }
            }

        } else
            Toast.makeText(
                context,
                getString(R.string.error_file_executong), Toast.LENGTH_LONG
            ).show()


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
       // Log.i(RaiseQueryActivity.TAG, "getRealPathFromURI: $contentUri")



        val cursor = activity!!.getContentResolver().query(contentUri, null, null, null, null)
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
    private var urlDownload: String? = null
    // init all views
    private fun initViews() {


        getMemberGroupNames()

    }


    private fun getMemberGroupNames() {

        mView.pb_grp_name.visibility = View.VISIBLE
        mDisposable.add(
            mService.getIndividualGroups(
                mLoginPreference.getStringData("id", "")!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        initRecyclerView(s.individualGroupsList)


                    } else {
                        Snackbar.make(mView.ll_grp_name, "No Data Found", Snackbar.LENGTH_LONG).show()
                    }
                }, { throwable ->
                    mView.pb_grp_name.visibility = View.GONE
                })
        )
    }

    private fun initRecyclerView(individualGroupsList: List<IndividualGrpNameModel.IndividualGroup>?) {
        mView.pb_grp_name.visibility = View.GONE
        mView.rv_group_name.hasFixedSize()
        mView.rv_group_name.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        ) as RecyclerView.LayoutManager?
        mView.rv_group_name.itemAnimator = DefaultItemAnimator()
        mAdapter = GroupNameAdapter(individualGroupsList!! , ar, requireContext(), this@GrpNameFragmentFragment)

        mView.rv_group_name.adapter = mAdapter

        mLoginPreference.savStringeData("master_admin",individualGroupsList.get(0).masterAdmin!!)
        mLoginPreference.savStringeData("Admin",individualGroupsList.get(0).groupAdmin!!)


//        if(mAdapter.arrayList.size>0){
//            mView.rl_bottom.visibility=View.VISIBLE
//
//        }

        mAdapter.notifyDataSetChanged()
      //
        //  recyclerView.getRecycledViewPool().setMaxRecycledViews(1,0);

    }

    override fun onStop() {
        mDisposable.clear()
        super.onStop()
    }

    override fun GrpNameListener(
        id: String, photo: String?, isMineQuery: Boolean,
        models: IndividualGrpNameModel.IndividualGroup,arrayList: ArrayList<String>
    ) {
        mLoginPreference.savStringeData("groupId", models.groupId!!)
       // mLoginPreference.savStringeData("Group_admin",models.groupAdmin!!)
        mLoginPreference.savStringeData("members",models.members!!)
        mLoginPreference.savStringeData("master_admin",models.masterAdmin!!)
        startActivity(Intent(requireActivity(), GroupChatActivity::class.java))
    }

    override fun GrpVisible(models: IndividualGrpNameModel.IndividualGroup, arrayList: ArrayList<String>)

    {
        if(arrayList.size>0)
        {
            mAdapter = GroupNameAdapter(listOf(models) , ar, requireContext(), this@GrpNameFragmentFragment)
            //startActivity(Intent(requireActivity(), GroupActivity::class.java))
            mView.rl_bottom.visibility=View.VISIBLE
          //  mAdapter.notifyDataSetChanged()
        }

    }

}
