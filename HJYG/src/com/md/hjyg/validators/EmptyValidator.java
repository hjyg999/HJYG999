package com.md.hjyg.validators;

import java.util.ArrayList;

import com.md.hjyg.Interface.IValidator;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.widget.TextView;

public class EmptyValidator implements IValidator {

	// default error string..
	String errorString = "Feilds Can not be empty";

	// list of input feilds in form..
	public ArrayList<TextView> formInputViews = new ArrayList<TextView>();
	private Activity activity;
	private DialogManager mDialog;

	// Constructor for single edit text with default empty msg.
	public EmptyValidator(Activity activity,TextView inputView) {

		formInputViews.add(inputView);
		this.activity = activity;
	}
	public EmptyValidator(Activity activity,TextView inputView,DialogManager mDialog) {
		
		formInputViews.add(inputView);
		this.activity = activity;
		this.mDialog = mDialog;
	}

	// Constructor for single edit text with default empty msg.
	public EmptyValidator(Activity activity,TextView inputView, String errorString) {
		this.activity = activity;
		this.errorString = errorString;

		formInputViews.add(inputView);
	}
	public EmptyValidator(Activity activity,TextView inputView, String errorString,DialogManager mDialog) {
		this.activity = activity;
		this.errorString = errorString;
		this.mDialog = mDialog;
		
		formInputViews.add(inputView);
	}

	private void showError( String errorString) {
		if (mDialog != null) {
			mDialog.initOneBtnDialog(false, errorString, null);
		}else {
			Constants.showOkPopup(activity, errorString);
		}
	}

	@Override
	public boolean validate() {
		for (TextView inputView : formInputViews) {

			if (inputView.getText().toString().trim().equalsIgnoreCase("")) {
				showError(errorString);
				return false;
			}

		}

		return true;
	}

}
