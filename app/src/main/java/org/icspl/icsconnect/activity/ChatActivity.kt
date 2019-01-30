package org.icspl.icsconnect.activity

import LoginPreference
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.util.Log.i
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_conversation.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.ChatAdapter
import org.icspl.icsconnect.models.Chat
import org.icspl.icsconnect.utils.Common
import java.util.*


class ChatActivity : AppCompatActivity() {

    private val mLoginPreference by lazy { LoginPreference.getInstance(this@ChatActivity) }
    private val mDisposable = CompositeDisposable()
    private val mService by lazy { Common.getAPI() }
    private var mAdapter: ChatAdapter? = null
    private lateinit var photoPath: String
    private lateinit var queryId: String
    private var mToolbar: Toolbar? = null


    companion object {
        val TAG = ChatActivity::class.java.canonicalName
    }


    private var toemp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar);


        if (intent != null) {
            photoPath = intent.getStringExtra("photo")
            queryId = intent.getStringExtra("queryId")
            toemp = intent.getStringExtra("toemp")
        }
        supportActionBar!!.setTitle("Query Id: $queryId")

        initRecyclerView()

    }

    private fun initRecyclerView() {

        recyclerView.setHasFixedSize(true)
        val manager = androidx.recyclerview.widget.LinearLayoutManager(this@ChatActivity)
        manager.reverseLayout = false;
        manager.stackFromEnd = true;
        recyclerView.layoutManager = manager
        mAdapter = ChatAdapter(getConversations())

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
            if (et_message.getText().toString() != "") {
                sendMessages()
            }
        })


    }

    // Send Messages to Server
    private fun sendMessages() {
        val data = ArrayList<Chat>()
        val item = Chat("2", et_message.getText().toString(), Common().getTime(), photoPath)
        mDisposable.add(
            mService.sendMessage(
                queryId, mLoginPreference.getStringData("id", "")!!,
                toemp!!, et_message.text.toString(), Common().getDateFormat(), null
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        if (s.get(0).response!! >= 1) {

                            Toast.makeText(
                                this@ChatActivity, "Query Sent Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            data.add(item)
                            mAdapter!!.addItem(data)
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter()!!.getItemCount() - 1)
                            et_message.setText("")
                        } else
                            Toast.makeText(
                                this@ChatActivity, "Query Failed to Send",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }, { throwable ->
                    Log.i(TAG, throwable.message)
                })
        )
    }

    private lateinit var menu: Menu

    private var item: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(org.icspl.icsconnect.R.menu.menu_open, menu)
        this.menu = menu
        item = menu.findItem(org.icspl.icsconnect.R.id.menu_close)
        return true
    }


    private fun getConversations(): ArrayList<Chat> {
        val data = ArrayList<Chat>()

        mDisposable.add(
            mService.getConversation(queryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        s.chatMessages?.forEach {
                            i(TAG, "MESSAGE: " + it.remarks)
                            val fromTo =
                                if (mLoginPreference.getStringData("id", "")!!.equals(it.toemp)) "1" else "2"
                            val item = Chat(fromTo, it.remarks!!, it.sendfrm!!.drop(14), photoPath)
                            data.add(item)
                        }
                        /* if (mLoginPreference.getStringData("id", "")!!
                                .equals(s.ShowCloseButton!![0].fromemp)) {
                            item!!.setVisible(true)
                        } else {
                            item!!.setVisible(false)
                        }*/
                    } else
                        Log.i(TAG, "Null Data:")
                }, { throwable ->
                    Log.i(TAG, throwable.message)
                })
        )
        return data
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_close -> startActivity(Intent(this@ChatActivity, RaiseQueryActivity::class.java))
        }
        return true
    }

}
