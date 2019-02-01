package org.icspl.icsconnect.activity


import LoginPreference
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.i
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.mancj.materialsearchbar.MaterialSearchBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_raise_query.*
import org.icspl.icsconnect.models.SearchModel
import org.icspl.icsconnect.utils.Common
import java.util.concurrent.TimeUnit
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener

import android.widget.TextView
import android.R.id.custom
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.FileChooserAdapter


class RaiseQueryActivity : AppCompatActivity(),
    PopupMenu.OnMenuItemClickListener {

    val context: Context = this

    private val mService by lazy { Common.getAPI() }
    val TAG = RaiseQueryActivity::class.java.simpleName
    private var searchList: MutableList<String> = ArrayList()
    private var searchHashList: HashMap<Int, SearchModel> = hashMapOf()

    private lateinit var searchBar: MaterialSearchBar
    private val mDisposeOn = CompositeDisposable()
    private val mLoginPreference by lazy { LoginPreference.getInstance(this@RaiseQueryActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.icspl.icsconnect.R.layout.activity_raise_query)

        //handleSearchChangeLisitenr()
        initSearchBar()
        btn_send_query.setOnClickListener { handleQuery() }


        btn_raised_attachment.setOnClickListener { ChooserDialog() }
    }

    // prepare data to send to server
    private fun handleQuery() {

        if (et_message_query.text!!.length == 0) {
            Toast.makeText(this@RaiseQueryActivity, "Error", Toast.LENGTH_SHORT).show()
            til_raised.isErrorEnabled = true
            til_raised.error = "Query can not be Empty"
            return
        }
        if (til_raised.isErrorEnabled)
            til_raised.isErrorEnabled = false

        mDisposeOn.add(
            mService.postQuery(
                mLoginPreference.getStringData("id", "")!!,
                tv_rased_to.tag.toString(),
                et_message_query.text.toString(),
                Common().getDateFormat(),
                mLoginPreference.getStringData("name", "")!!,
                et_message_query.text.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it != null) {
                        if (it.get(0).response!! >= 1) {
                            Toast.makeText(this@RaiseQueryActivity, "Query Raised Successfully", Toast.LENGTH_SHORT)
                                .show()
                        } else
                            Toast.makeText(this@RaiseQueryActivity, "Failed to Raised Query", Toast.LENGTH_SHORT)
                                .show()
                    }
                })
    }

    private fun initSearchBar() {
        searchBar = findViewById(org.icspl.icsconnect.R.id.searchView);
        searchBar.setHint("Search Opposite Name");
        searchBar.inflateMenu(org.icspl.icsconnect.R.menu.menu_open)
        searchBar.setCardViewElevation(10)
        searchBar.setMaxSuggestionCount(10)
        searchList.add("Sanjay")
        searchList.add("Parmesh")
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
                if (editable.toString().length >= 1)
                    setUpSearchObservable(editable.toString())
            }
        })

        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onButtonClicked(buttonCode: Int) {}
            override fun onSearchStateChanged(enabled: Boolean) {}

            override fun onSearchConfirmed(text: CharSequence?) {
                i(TAG, "Search Confirmed")
                setUpSearchObservable(text.toString())
            }
        })

    }

private fun ChooserDialog(){

    val dialog = DialogPlus.newDialog(this)
        .setAdapter(FileChooserAdapter(this@RaiseQueryActivity,1))
        .setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(dialog: DialogPlus, item: Any, view: View, position: Int) {

            }
        })
        .setExpanded(false)
        .setGravity(Gravity.CENTER)
        .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)  // or any custom width ie: 300
        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)

        // This will enable the expand feature, (similar to android L share dialog)
        .create()
    dialog.show()
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
}
