package br.gov.am.tce.auditor.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adriano on 14/03/2018.
 */

public class Contract implements Parcelable {
    private String key;
    private String startDate;
    private String status;
    private String expense;
    private String contractor;
    private List<Metering> meteringList = new ArrayList<>();

    public Contract(Parcel in) {
        key = in.readString();
        startDate = in.readString();
        status = in.readString();
        expense = in.readString();
        contractor = in.readString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(key);
        out.writeString(startDate);
        out.writeString(status);
        out.writeString(expense);
        out.writeString(contractor);
    }

    public static final Parcelable.Creator<Contract> CREATOR = new Parcelable.Creator<Contract>() {
        public Contract createFromParcel(Parcel in) {
            return new Contract(in);
        }

        public Contract[] newArray(int size) {
            return new Contract[size];
        }
    };
}
