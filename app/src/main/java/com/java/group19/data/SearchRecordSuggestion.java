package com.java.group19.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by liena on 17/9/9.
 */

public class SearchRecordSuggestion implements SearchSuggestion {

    private String mRecord;

    public SearchRecordSuggestion(String suggestion) {
        mRecord = suggestion;
    }

    public SearchRecordSuggestion(Parcel sourse) {
        this.mRecord = sourse.readString();
    }

    @Override
    public String getBody() {
        return mRecord;
    }

    public static final Creator<SearchRecordSuggestion> CREATOR = new Creator<SearchRecordSuggestion>() {
        @Override
        public SearchRecordSuggestion createFromParcel(Parcel parcel) {
            return new SearchRecordSuggestion(parcel);
        }

        @Override
        public SearchRecordSuggestion[] newArray(int i) {
            return new SearchRecordSuggestion[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mRecord);
    }
}
