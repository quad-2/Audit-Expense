package com.example.android.auditexpense;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ExpenseList implements Parcelable {
    @Expose
    private List<ExpenseDetail> expenses = new ArrayList<>();

    public ExpenseList(List<ExpenseDetail> expenses) {
        this.expenses = expenses;
    }

    public List<ExpenseDetail> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDetail> expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {
        return "ExpenseList{" +
                "expenses=" + expenses +
                '}';
    }

    protected ExpenseList(Parcel in) {
        if (in.readByte() == 0x01) {
            expenses = new ArrayList<ExpenseDetail>();
            in.readList(expenses, ExpenseDetail.class.getClassLoader());
        } else {
            expenses = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (expenses == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(expenses);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExpenseList> CREATOR = new Parcelable.Creator<ExpenseList>() {
        @Override
        public ExpenseList createFromParcel(Parcel in) {
            return new ExpenseList(in);
        }

        @Override
        public ExpenseList[] newArray(int size) {
            return new ExpenseList[size];
        }
    };
}