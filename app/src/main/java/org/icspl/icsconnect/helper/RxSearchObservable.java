package org.icspl.icsconnect.helper;

import android.widget.SearchView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxSearchObservable {

    public RxSearchObservable() {
    }

    public static Observable<String> fromView(SearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onNext(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                subject.onNext(text);
                return true;
            }
        });

        return subject;
    }


    public static String getText(final SearchView searchView) {

        final String[] searched = new String[1];
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searched[0] = s;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                searched[0] = text;
                return true;
            }
        });

        return searched[0];
    }


}
