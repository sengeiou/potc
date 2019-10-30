package com.poct.android.entity;

public class UpDataModel {
	
	public int mVersionCode;
	public String mVersionName;
	public String mUpDataImf;
	public boolean ignore = false;
	
	public UpDataModel(int mVersionCode, String mVersionName, String mUpDataImf)
	{
		this.mVersionCode = mVersionCode;
		this.mVersionName = mVersionName;
		this.mUpDataImf = mUpDataImf;
		
	}
	
}
