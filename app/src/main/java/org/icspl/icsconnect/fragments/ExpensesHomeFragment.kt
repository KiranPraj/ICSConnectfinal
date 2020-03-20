package org.icspl.icsconnect.fragments

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.fragment_expenses_home.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.WeeklyExpenseModel
import org.icspl.icsconnect.preferences.LoginPreference
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import kotlinx.android.synthetic.main.activity_raise_query.*
import kotlinx.android.synthetic.main.activity_time_sheet.*
import kotlinx.android.synthetic.main.activity_time_sheet.selectdate
import kotlinx.android.synthetic.main.weekly_expenses_layout.*
import org.icspl.icsconnect.BuildConfig
import org.icspl.icsconnect.activity.LoginActivity
import org.icspl.icsconnect.activity.RaiseQueryActivity
import org.icspl.icsconnect.adapters.ITopicClickListener
import org.icspl.icsconnect.adapters.OutStationAdapter

import org.icspl.icsconnect.adapters.WeeklyExpenseAdapter
import org.icspl.icsconnect.models.OpenMessage
import org.icspl.icsconnect.models.OutStationExpensesModel
import org.icspl.icsconnect.utils.Common
import org.icspl.icsconnect.utils.FileManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.ArrayList










class ExpensesHomeFragment:Fragment(), ITopicClickListener {
    override fun photoCallback(btnPhoto: Button) {

        checkPermissions(btnPhoto)
    }

    private lateinit var mContext: Context
    private lateinit var mView: View
    private lateinit var mActivity: FragmentActivity
    private lateinit var master_admin:String
    private lateinit var user:String
    private lateinit var mLoginPreference:LoginPreference
    internal lateinit var picker: DatePickerDialog
     var  radiobtn_text:String?=null
    private lateinit var weekly_list:ArrayList<WeeklyExpenseModel>
    private lateinit var outstation_list:ArrayList<OutStationExpensesModel>
    private lateinit var outstation_list2:ArrayList<OutStationExpensesModel>
    private lateinit var mAdapter: WeeklyExpenseAdapter
    private lateinit var adapter_outstation:OutStationAdapter
    private val mRandom = Random()
    val viewHolder: RecyclerView.ViewHolder? = null
    private val mService by lazy { Common.getAPI() }
    private lateinit var imageBtnFile: File

    private lateinit var imageToUploadUri: Uri
    private lateinit var btnPhoto:Button
    companion object {

        fun newInstance(): ExpensesHomeFragment {
            return ExpensesHomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView= inflater.inflate(R.layout.fragment_expenses_home, container, false)
        mLoginPreference= LoginPreference.getInstance(mView.context)
        master_admin=mLoginPreference.getStringData("masteradmin","").toString()
        user=mLoginPreference.getStringData("user","").toString()
        // linearview(mView)
        mContext = mView.context
        mActivity = activity as FragmentActivity
        mView.btn_addnewrow.setOnClickListener {
        if(radiobtn_text.toString()=="OutStation")
        {
            addOutstationRow()
        }
        else
        {
            addWeeklyRow()
        }
    }
    mView.btn_removenewrow.setOnClickListener {
        removerow(radiobtn_text.toString())
    }
        mView.btn_addnewrow_out.setOnClickListener { addrow2() }
        mView.btn_removenewrow_out.setOnClickListener { removerow2() }
        mView.selectdate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            picker = DatePickerDialog(mContext,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectdate.text = (monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year
                }, year, month, day
            )
            picker.datePicker.maxDate = cldr.timeInMillis
            picker.show()
            var calenderdate=selectdate.text.toString()

        }

   mView.text_bording.setOnClickListener {
        if (mView.linear_recycle.visibility==View.VISIBLE)
        {
            mView.linear_recycle.visibility=View.GONE
        }
       else
        {
            mView.linear_recycle.visibility=View.VISIBLE
        }

   }

        mView.text_local.setOnClickListener {
            if (mView.linear_recycle_local.visibility==View.VISIBLE)
            {
                mView.linear_recycle_local.visibility=View.GONE
            }
            else
            {
                mView.linear_recycle_local.visibility=View.VISIBLE
            }
        }

      init(mView)



        return mView
    }

    private fun checkPermissions(btnPhoto: Button) {
        //check Permission

        Dexter.withActivity(mContext as Activity?)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        pickerDialog(btnPhoto)
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
        val builder = AlertDialog.Builder(mContext)
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
        val uri = Uri.fromParts("package", mContext.getPackageName(), null)
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

    private fun pickerDialog(btnPhoto: Button) {
        this@ExpensesHomeFragment.btnPhoto =btnPhoto
        val view = layoutInflater.inflate(R.layout.griddialog_filechooser, null)
        val cameraLinearLayout = view.findViewById(R.id.ll_camera) as LinearLayout
        val docLinearLayout = view.findViewById(R.id.ll_doccument) as LinearLayout
        val dialog = AlertDialog.Builder(mContext)
            .setView(view)
            .setPositiveButton(null, null)
            .setNegativeButton(null, null)

            .create()
        cameraLinearLayout.setOnClickListener {
            dialog.dismiss()
            pickImage(btnPhoto)
        }
        docLinearLayout.setOnClickListener {
            dialog.dismiss()
            picDocuments(btnPhoto)
        }

        dialog.show()
    }

    private val DOCCUMENT_REQUEST_CODE: Int = 2102
    private val IMAGE_REQUEST_CODE: Int = 2103

    private fun picDocuments(btnPhoto: Button) {

        try {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
          //  val fileIntentt=Intent(Intent.ACTION_PICK,Intent.ACTION_CHOOSER)
            //                        Uri uri = Uri.parse(Environment.getRootDirectory().getAbsolutePath());
            //                        fileIntent.setData(uri);
            fileIntent.type = "image/*|application/pdf"
           // fileIntent.type="image/*"
         //   fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)

            val extraMimeTypes = arrayOf(
                "image/*",
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
         //   fileIntent.setAction(Intent.ACTION_OPEN_DOCUMENT)
            startActivityForResult(fileIntent, DOCCUMENT_REQUEST_CODE)
        } catch (e: Exception) {

            Toast.makeText(mContext,"Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun pickImage(btnPhoto: Button) {
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
            mContext, BuildConfig.APPLICATION_ID + ".provider", imageBtnFile
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
                imageActivityResult(btnPhoto)
            }
            DOCCUMENT_REQUEST_CODE -> {
                documentResultHandler(resultCode, data, btnPhoto)
            }
        }
    }

    private fun imageActivityResult(btnPhoto: Button) {
        run {

           // Log.i(RaiseQueryActivity.TAG, "onActivityResult: $imageToUploadUri")

            try {
                object : AsyncTask<Void, Void, String>() {
                    override fun doInBackground(vararg params: Void): String {

                        val path = compressImage(imageBtnFile.toString())

                       // Log.i(RaiseQueryActivity.TAG, "doInBackground: path: $path")

                        return path
                    }

                    override fun onPostExecute(path: String) {

                        try {
                            if (path != null) {
                                btnPhoto.apply {
                                    text = "Attached"
                                    setBackgroundColor(
                                        ContextCompat.getColor(
                                            mContext, android.R.color.holo_green_light
                                        )
                                    )
                                    btnPhoto.tag = path
                                    Toast.makeText(
                                        mContext, "File Attached Successfully", Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else
                                Toast.makeText(
                                    mContext, "Failed to Attach File", Toast.LENGTH_SHORT
                                ).show()


                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                mContext, "Failed to Attach File", Toast.LENGTH_SHORT
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
        Log.i(RaiseQueryActivity.TAG, "getRealPathFromURI: $contentUri")


        val cursor = mContext.getContentResolver().query(contentUri, null, null, null, null)
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
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {

            val selectedImage = data.data
            if (selectedImage != null) {
                val picturePath = FileManager.getPath(mContext, selectedImage)
           //     Log.i(RaiseQueryActivity.TAG, "documentResultHandler: " + picturePath!!)
                try {
                    val file = File(picturePath)
                    if (file.exists()) {
                        if (file.length() / 1024 >= 5120) { //15 mb
                            Toast.makeText(mContext, "File Size limit is upto 5MB", Toast.LENGTH_LONG)
                                .show()
                            return
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                println("picturePath +" + picturePath)  //path of sdcard
               if (picturePath!=null&&picturePath!="")
               {
                   fileButton.apply {
                       setBackgroundColor(
                           ContextCompat.getColor(
                               mContext, android.R.color.holo_green_light
                           )
                       )
                       btnPhoto.tag = picturePath
                       text = mContext.getString(R.string.attached)
                       Toast.makeText(
                           mContext, "File Attached Successfully", Toast.LENGTH_SHORT
                       ).show()
                   }

               }
               else {
                   fileButton.apply {
                       setBackgroundColor(
                           ContextCompat.getColor(
                               mContext, android.R.color.white
                           )
                       )
                       btnPhoto.tag = picturePath
                       text = mContext.getString(R.string.not_attached)

                       Toast.makeText(
                           mContext, "Failed To File Attached", Toast.LENGTH_SHORT
                       ).show()
                   }
               }

            }

        } else
            Toast.makeText(
                mContext,
                getString(R.string.error_file_executong), Toast.LENGTH_LONG
            ).show()


    }


    private fun init(mView: View?)
    {
        mContext = mView!!.context
        weekly_list = ArrayList<WeeklyExpenseModel>()
        outstation_list=ArrayList<OutStationExpensesModel>()
        outstation_list2=ArrayList<OutStationExpensesModel>()
      
        addWeeklyRow()
        selectdatefunction()
        mView.btn_addnewrow_out.visibility=View.GONE
        mView.btn_removenewrow_out.visibility=View.GONE
        mView.rv_expenses_outstation.visibility=View.GONE
         mView.expenses_radio_grp.check(org.icspl.icsconnect.R.id.r_first)

        radiobtn_text= mView.r_first.text.toString()
       // expensedetails(radiobtn_text!!)
        // Get radio group selected item using on checked change listener
    mView.expenses_radio_grp.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->

                val radio: RadioButton = mView.findViewById(checkedId)
                radiobtn_text= radio.text.toString()
                Toast.makeText(mContext," On checked change : $radiobtn_text",
                    Toast.LENGTH_SHORT).show()
                if (radiobtn_text=="NA")
                {
                    mView.rv_expenses.visibility=View.GONE
                    mView.linear_na.visibility=View.VISIBLE
                   mView.btn_addnewrow.visibility=View.GONE
                    mView.btn_removenewrow.visibility=View.GONE
                    mView.btn_addnewrow_out.visibility=View.GONE
                    mView.btn_removenewrow_out.visibility=View.GONE
                    mView.rv_expenses_outstation.visibility=View.GONE
                    mView.card_for_boarding.visibility=View.GONE
                    mView.card_for_local.visibility=View.GONE

                }
                else if(radiobtn_text=="OutStation")
                {
                    weekly_list.clear()
                    outstation_list.clear()
                    outstation_list2.clear()
                    mView.rv_expenses.visibility=View.VISIBLE
                    mView.linear_na.visibility=View.VISIBLE
                    mView.btn_addnewrow.visibility=View.VISIBLE
                    mView.btn_removenewrow.visibility=View.VISIBLE
                    mView.btn_addnewrow_out.visibility=View.VISIBLE
                    mView.btn_removenewrow_out.visibility=View.VISIBLE
                    mView.rv_expenses_outstation.visibility=View.VISIBLE
                    mView.card_for_boarding.visibility=View.VISIBLE
                    mView.card_for_local.visibility=View.VISIBLE
                    //expensedetails(radiobtn_text!!)
                    addOutstationRow()
                    addrow2()

                }
                else
                {

                    mView.btn_removenewrow.visibility=View.VISIBLE
                    mView.rv_expenses.visibility=View.VISIBLE
                    mView.linear_na.visibility=View.VISIBLE
                    mView.btn_addnewrow.visibility=View.VISIBLE
                    mView.btn_addnewrow_out.visibility=View.GONE
                    mView.btn_removenewrow_out.visibility=View.GONE
                    mView.rv_expenses_outstation.visibility=View.GONE
                    mView.card_for_boarding.visibility=View.GONE
                    mView.card_for_local.visibility=View.VISIBLE
                    weekly_list.clear()
                    outstation_list.clear()
                    outstation_list2.clear()
                    addWeeklyRow()
                }


            })



    }

    private fun selectdatefunction() {
        var emp_code=mLoginPreference.getStringData("id","")
        mView.selectdate.addTextChangedListener(object: TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(mContext, DialogTypes.TYPE_CUSTOM,"data.json")
                    .setTitle("Loading")
                    .setDescription("Please Wait")
                    .build()
                alertDialog.setCancelable(false)
                alertDialog.show()


                var date=s?.toString()!!
                mService!!.getDateStatuss(emp_code!!,date)
                    .enqueue(object: Callback<OpenMessage> {
                        override fun onFailure(call: Call<OpenMessage>, t: Throwable) {
                            alertDialog.dismiss()
                            var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(mContext, DialogTypes.TYPE_CUSTOM,"Error.json")
                                .setTitle("Error")
                                .setPositiveListener(object : ClickListener {
                                    override fun onClick(dialog: LottieAlertDialog) {
                                        selectdate.setText("SELECT")
                                        dialog.dismiss()
                                    }

                                })
                                .setPositiveButtonColor(resources.getColor(R.color.red_app))
                                .setPositiveText("Ok")
                                .setDescription("Please Check internet connection or try again")
                                .build()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }

                        override fun onResponse(call: Call<OpenMessage>, response: Response<OpenMessage>) {
                            if(response.isSuccessful&& response.body()!!.openMessage!!.size!=0)
                            {
                                var exitstatus=response.body()!!.openMessage!!.get(0).exitStatus!!
                                if(exitstatus.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(mContext)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("ICS")
                                        .setMessage("You are no longer the member of the ICS Thanks for choosing ICS ")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            val sharedPreferences = mContext.getSharedPreferences(getString(R.string.login), Context.MODE_PRIVATE)
                                            val edit = sharedPreferences.edit()

                                            edit.remove("id")
                                            //edit.clear()
                                            edit.apply()
                                            startActivity(
                                                Intent(mContext,
                                                    LoginActivity::class.java)
                                            )

                                        }.show()

                                }
                                else if(response.body()!!.openMessage!!.get(0).sentSalaryStatus!!.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(mContext)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Salary Released")
                                        .setMessage("choosen date salary has been released")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                           // restrict.visibility=View.GONE
                                        }.show()

                                }
                                else if(response.body()!!.openMessage!!.get(0).protectDateStatus!!.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(mContext)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Salary Released")
                                        .setMessage("choosen date already filled with holiday")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                          //  restrict.visibility=View.GONE
                                        }.show()

                                }
                                else if(response.body()!!.openMessage!!.get(0).docUploadStatus!!.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(mContext)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Upload Documents")
                                        .setMessage("Kindly Upload all the necessary documents to fill the timesheet")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            //restrict.visibility=View.GONE
                                        }.show()
                                    //restrict.visibility=View.GONE

                                }
                                else if(response.body()!!.openMessage!!.get(0).prevoiusFilled!!.trim().length>2)
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(mContext)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Pending Previous Date Timesheet")
                                        .setMessage("Kindly select the date "+response.body()!!.openMessage!!.get(0).prevoiusFilled!!.trim())
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            //restrict.visibility=View.GONE
                                        }.show()
                                    //restrict.visibility=View.GONE
                                }
                                else if(response.body()!!.openMessage!!.get(0).sundayStatus!!.equals("Y")||response.body()!!.openMessage!!.get(0).saturdayStatus!!.equals("Y")||response.body()!!.openMessage!!.get(0).iCSHolidayStatus!!.equals("Y"))
                                {
                                    var time:MutableList<String> = java.util.ArrayList()
                                    time.add("H")
                                    time.add("8")
                                    time.add("6")
                                    time.add("4")
                                    val adapter =  ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, time)
                                    workinghr.adapter = adapter
                                    adapter.notifyDataSetChanged()
                                    alertDialog.dismiss()
                                    //holidayrestrict.visibility=View.GONE
                                }
                                else
                                {
                                    alertDialog.dismiss()
                                    //restrict.visibility=View.VISIBLE

                                }

                            }
                            else
                            {
                                alertDialog.dismiss()
                                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(mContext, DialogTypes.TYPE_CUSTOM,"Error.json")
                                    .setTitle("Error")
                                    .setDescription("Response Error try again")
                                    .build()
                                alertDialog.show()

                            }
                        }

                    })
            }



            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }
        )
    }


    private fun addOutstationRow() {
        val socVPModel1 =
            OutStationExpensesModel("", "", "n", "", "", "", "", "", "", "", "", "", "", "")
        outstation_list.add(socVPModel1)
        adapter_outstation = OutStationAdapter(mContext, outstation_list,this@ExpensesHomeFragment)
        mView.rv_expenses.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses.setAdapter(adapter_outstation)
        adapter_outstation.notifyDataSetChanged()
    }

    private fun addWeeklyRow() {

        val socVPModel = WeeklyExpenseModel("", "", "", "", "", "", "", "", "", "", "", "", "")
        weekly_list.add(socVPModel)
        //   weekly_list.remove(socVPModel)
        mAdapter = WeeklyExpenseAdapter(mContext, weekly_list,this@ExpensesHomeFragment)
        mView.rv_expenses.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses.setAdapter(mAdapter)
        mAdapter.notifyDataSetChanged()
    }

    private  fun removerow(radiobtnText: String)
{
    if(radiobtnText=="Weekly") {
        if (weekly_list.size<2)
        {
            return
        }

            var socVPModel=weekly_list.get(weekly_list.size-1)
        weekly_list.remove(socVPModel)
        mAdapter.notifyItemRemoved(weekly_list.size)
        mAdapter.notifyDataSetChanged()
    }
    else
    {
        if (outstation_list.size<2)
        {
            return
        }
        var socVPModel=outstation_list.get(outstation_list.size-1)
        outstation_list.remove(socVPModel)
        adapter_outstation.notifyItemRemoved(outstation_list.size)
        adapter_outstation.notifyDataSetChanged()
    }
}
    private  fun removerow2()
    {
        if (outstation_list2.size<2)
        {
            return
        }
        var socVPModel2=outstation_list2.get(outstation_list2.size-1)
        outstation_list2.remove(socVPModel2)
        adapter_outstation.notifyItemRemoved(outstation_list2.size)
        adapter_outstation.notifyDataSetChanged()
    }
    private fun addrow2()
    {
        val socVPModel2 = OutStationExpensesModel("", "", "y", "", "", "","","","", "","","", "", "")
        outstation_list2.add(socVPModel2)
        adapter_outstation = OutStationAdapter(mContext, outstation_list2,this@ExpensesHomeFragment)
        mView.rv_expenses_outstation.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses_outstation.setAdapter(adapter_outstation)
        adapter_outstation.notifyDataSetChanged()
    }
}