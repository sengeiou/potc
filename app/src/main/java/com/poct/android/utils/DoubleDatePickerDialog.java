/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poct.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.poct.R;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * A simple dialog containing an {@link DatePicker}.
 *
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 */
public class DoubleDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener, OnTimeChangedListener
{

	private static final String START_YEAR = "start_year";
	private static final String END_YEAR = "end_year";
	private static final String START_MONTH = "start_month";
	private static final String END_MONTH = "end_month";
	private static final String START_DAY = "start_day";
	private static final String END_DAY = "end_day";
	private static final String HOUR = "hour";
	private static final String MINIUTE = "miniute";

	private  DatePicker mDatePicker_start;
	private  DatePicker mDatePicker_end;
	private  TimePicker mTimePicker;
	private  OnDateSetListener mCallBack;
	private  OnDoubleDateSetListener mCallBack2;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener
	{


		void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute);
	}

	public interface OnDoubleDateSetListener
	{


		void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int endYear, int endMonthOfYear, int endDayOfMonth);
	}

	public DoubleDatePickerDialog(Context context, int theme, OnDoubleDateSetListener callBack, String title1, String title2,
                                  String time1, String time2, boolean isDayVisible)
	{
		super(context, theme);
		Calendar c1 = AppUtils.getCalendar(time1+" 00:00");
		Calendar c2 = AppUtils.getCalendar(time2+" 00:00");
		mCallBack2 = callBack;
		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, context.getString(R.string.button_word_ok), this);
		setButton(BUTTON_NEGATIVE, context.getString(R.string.button_word_cancle), this);
		setIcon(0);
		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.double_date_picker_dialog, null);
		TextView mTextView = (TextView) view.findViewById(R.id.title1);
		mTextView.setText(title1);
		mTextView = (TextView) view.findViewById(R.id.title2);
		mTextView.setText(title2);
		setView(view);
		mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
		mDatePicker_start.init(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), this);
		mDatePicker_end = (DatePicker) view.findViewById(R.id.datePickerEnd);
		mDatePicker_end.init(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE),this);
		// 如果要隐藏当前日期，则使用下面方法。
		if (isDayVisible == false)
		{
			hidDay(mDatePicker_start);
		}
	}


	public DoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, String title, String time, boolean isDayVisible)
	{
		super(context, theme);
		Calendar c1 = AppUtils.getCalendar(time+" 00:00");
		mCallBack = callBack;
		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, context.getString(R.string.button_word_ok), this);
		setButton(BUTTON_NEGATIVE, context.getString(R.string.button_word_cancle), this);
		setIcon(0);
		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		TextView mTextView = (TextView) view.findViewById(R.id.title);
		mTextView.setText(title);
		setView(view);
		mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
		mTimePicker = (TimePicker) view.findViewById(R.id.timepicker);
		mDatePicker_start.init(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), this);
		mTimePicker.setVisibility(View.GONE);
		// 如果要隐藏当前日期，则使用下面方法。
		if (!isDayVisible)
		{
			hidDay(mDatePicker_start);
		}
	}



	public DoubleDatePickerDialog(Context context, OnDateSetListener callBack, String title, String time)
	{
		super(context, 3);
		Calendar c1 = AppUtils.getCalendar(time);
		mCallBack = callBack;
		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, context.getString(R.string.button_word_ok), this);
		setButton(BUTTON_NEGATIVE, context.getString(R.string.button_word_cancle), this);
		setIcon(0);
		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		TextView mTextView = (TextView) view.findViewById(R.id.title);
		mTextView.setText(title);
		setView(view);
		mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
		mDatePicker_start.setVisibility(View.GONE);
		mTimePicker = (TimePicker) view.findViewById(R.id.timepicker);
		mTimePicker.setIs24HourView(true);
		mTimePicker.setOnTimeChangedListener(this);
		mTimePicker.setCurrentHour(c1.get(Calendar.HOUR_OF_DAY));
		mTimePicker.setCurrentMinute(c1.get(Calendar.MINUTE));
	}

	
	public DoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, String title, String time)
	{
		super(context, theme);
		Calendar c1 = AppUtils.getCalendar(time);
		mCallBack = callBack;

		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, context.getString(R.string.button_word_ok), this);
		setButton(BUTTON_NEGATIVE, context.getString(R.string.button_word_cancle), this);
		setIcon(0);
		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		TextView mTextView = (TextView) view.findViewById(R.id.title);
		mTextView.setText(title);
		setView(view);
		mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
		mTimePicker = (TimePicker) view.findViewById(R.id.timepicker);
		mDatePicker_start.init(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), this);
		mTimePicker.setIs24HourView(true);
		mTimePicker.setOnTimeChangedListener(this);
		mTimePicker.setCurrentHour(c1.get(Calendar.HOUR_OF_DAY));
		mTimePicker.setCurrentMinute(c1.get(Calendar.MINUTE));

	}

	/**
	 * 隐藏DatePicker中的日期显示
	 * 
	 * @param mDatePicker
	 */
	private void hidDay(DatePicker mDatePicker)
	{
		Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
		LinearLayout l1 = (LinearLayout) mDatePicker.getChildAt(0);
		LinearLayout l2 = (LinearLayout) l1.getChildAt(0);
		l2.getChildAt(2).setVisibility(View.GONE);
	}

	public void onClick(DialogInterface dialog, int which)
	{

		if (which == BUTTON_POSITIVE) tryNotifyDateSet();
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day)
	{
		if (view.getId() == R.id.datePickerStart) mDatePicker_start.init(year, month, day, this);

	}

	private void tryNotifyDateSet()
	{
		if (mCallBack != null)
		{
			mDatePicker_start.clearFocus();
			mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(), mDatePicker_start.getDayOfMonth(),
					mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute());
		}
		else if(mCallBack2 != null)
		{
			mDatePicker_start.clearFocus();
			mDatePicker_end.clearFocus();
			mCallBack2.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(), mDatePicker_start.getDayOfMonth(),
					mDatePicker_end.getYear(), mDatePicker_end.getMonth(), mDatePicker_end.getDayOfMonth());
		}
	}

	@Override
	protected void onStop()
	{
		// tryNotifyDateSet();
		super.onStop();
	}

	@Override
	public Bundle onSaveInstanceState()
	{
		Bundle state = super.onSaveInstanceState();
		state.putInt(START_YEAR, mDatePicker_start.getYear());
		state.putInt(START_MONTH, mDatePicker_start.getMonth());
		state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		int start_year = savedInstanceState.getInt(START_YEAR);
		int start_month = savedInstanceState.getInt(START_MONTH);
		int start_day = savedInstanceState.getInt(START_DAY);
		mDatePicker_start.init(start_year, start_month, start_day, this);

	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
	{
		// TODO Auto-generated method stub
		mTimePicker.setCurrentHour(hourOfDay);
		mTimePicker.setCurrentMinute(minute);
	}
}
