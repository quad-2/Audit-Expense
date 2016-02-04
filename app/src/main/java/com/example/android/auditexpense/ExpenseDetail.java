package com.example.android.auditexpense;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ExpenseDetail implements Parcelable {

    @SerializedName("id")
    @Expose
    private String expenseId;
    @SerializedName("description")
    @Expose
    private String expenseDescription;
    @Expose
    private int amount;
    @Expose
    private String category;
    @Expose
    private String time;
    @Expose
    private String state;

    public ExpenseDetail(String expenseId, String expenseDescription, int amount, String category, String time, String state) {
        this.expenseId = expenseId;
        this.expenseDescription = expenseDescription;
        this.amount = amount;
        this.category = category;
        this.time = time;
        this.state = state;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ExpenseDetail{" +
                "expenseId=" + expenseId +
                ", expenseDescription='" + expenseDescription + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    protected ExpenseDetail(Parcel in) {
        expenseId = in.readString();
        expenseDescription = in.readString();
        amount = in.readInt();
        category = in.readString();
        time = in.readString();
        state = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expenseId);
        dest.writeString(expenseDescription);
        dest.writeInt(amount);
        dest.writeString(category);
        dest.writeString(time);
        dest.writeString(state);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExpenseDetail> CREATOR = new Parcelable.Creator<ExpenseDetail>() {
        @Override
        public ExpenseDetail createFromParcel(Parcel in) {
            return new ExpenseDetail(in);
        }

        @Override
        public ExpenseDetail[] newArray(int size) {
            return new ExpenseDetail[size];
        }
    };
}