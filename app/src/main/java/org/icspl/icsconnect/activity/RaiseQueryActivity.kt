package org.icspl.icsconnect.activity


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.i
import android.widget.SearchView
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_raise_query.*
import org.icspl.icsconnect.adapters.SearchAdapter
import org.icspl.icsconnect.utils.Common
import java.util.concurrent.TimeUnit

class RaiseQueryActivity : AppCompatActivity() {


    private val mService by lazy { Common.getAPI() }
    val TAG = RaiseQueryActivity::class.java.simpleName
    private lateinit var searchList: ArrayList<String>
    private lateinit var mAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.icspl.icsconnect.R.layout.activity_raise_query)

        handleSearchChangeLisitenr()
        initRV()
    }

    private fun initRV() {
        searchList = mutableListOf<String>() as ArrayList<String>
        searchView.onActionViewExpanded();

        val mLayoutManager = LinearLayoutManager(this@RaiseQueryActivity, LinearLayoutManager.VERTICAL, false)
        rv_search_results.setHasFixedSize(true)
        rv_search_results.layoutManager = mLayoutManager
        rv_search_results.itemAnimator = DefaultItemAnimator()
        mAdapter = SearchAdapter(searchList, this@RaiseQueryActivity)
        rv_search_results.adapter = mAdapter
    }

    private fun handleSearchChangeLisitenr() {
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

    }


    private fun setUpSearchObservable(searched: String) {
        mService.search(searched)
            //     RxSearchObservable.fromView(searchView)
            .debounce(200, TimeUnit.MILLISECONDS)
            .filter(Predicate {
                if (it.isNotEmpty())
                    true
                else false
            })
            .distinctUntilChanged()
            .switchMap(object : Function<List<String>, ObservableSource<List<String>>> {
                override fun apply(t: List<String>): ObservableSource<List<String>>? {
                    return dataFromNetwork(t)
                }

            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<String>> {
                override fun accept(result: List<String>) {
                    searchList.clear()
                    result.forEach {
                        i(TAG, "DATA: $it")
                        searchList.add(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
            })
    }

    /**
     * Simulation of network data
     */
    private fun dataFromNetwork(query: List<String>): Observable<List<String>> {
        return Observable.just(true)
            .delay(1, TimeUnit.SECONDS)
            .map(object : Function<Boolean, List<String>> {
                override fun apply(t: Boolean): List<String> {
                    return query
                }
            })
    }
}
