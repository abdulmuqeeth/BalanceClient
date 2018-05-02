package abdulmuqeeth.uic.com.balancecommon;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyCash implements Parcelable {
    int mDay = 25 ;
    int mMonth = 4 ;
    int mYear = 2018 ;
    int mCash = 8988 ;
    String mDayOfWeek = "Wednesday" ;

    public DailyCash(int mYear, int mMonth, int mDate, String mDayOfWeek, int mCash ) {
        this.mDay = mDay;
        this.mMonth = mMonth;
        this.mYear = mYear;
        this.mCash = mCash;
        this.mDayOfWeek = mDayOfWeek;
    }

    public DailyCash(Parcel in) {
        mDay = in.readInt() ;
        mMonth = in.readInt() ;
        mYear = in.readInt() ;
        mCash = in.readInt() ;
        mDayOfWeek = in.readString() ;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mDay);
        out.writeInt(mMonth) ;
        out.writeInt(mYear) ;
        out.writeInt(mCash) ;
        out.writeString(mDayOfWeek) ;
    }

    public static final Creator<DailyCash> CREATOR
            = new Creator<DailyCash>() {

        public DailyCash createFromParcel(Parcel in) {
            return new DailyCash(in) ;
        }

        public DailyCash[] newArray(int size) {
            return new DailyCash[size];
        }
    };

    public int describeContents()  {
        return 0 ;
    }

    public int getmDay() {
        return mDay;
    }

    public int getmMonth() {
        return mMonth;
    }

    public int getmYear() {
        return mYear;
    }

    public int getmCash() {
        return mCash;
    }

    public String getmDayOfWeek() {
        return mDayOfWeek;
    }

}
