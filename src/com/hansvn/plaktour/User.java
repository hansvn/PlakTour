package com.hansvn.plaktour;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	//parcelling part:	
	public User(Parcel in) {
		
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in); 
		}
		
		public User[] newArray(int size) {
			return new User[size];
		}
	};

}