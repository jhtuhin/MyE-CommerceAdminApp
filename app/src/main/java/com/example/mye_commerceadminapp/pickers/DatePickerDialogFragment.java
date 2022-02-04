package com.example.mye_commerceadminapp.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mye_commerceadminapp.utils.AdminConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar=Calendar .getInstance(Locale.getDefault());
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        final Calendar calendar=Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        final String selectedDate=simpleDateFormat.format(calendar.getTime());
        final Bundle bundle=new Bundle();
        bundle.putString(AdminConstants.DATE_KEY,selectedDate);
        bundle.putInt(AdminConstants.YEAR,year);
        bundle.putInt(AdminConstants.MONTH,month);
        bundle.putInt(AdminConstants.DAY,dayOfMonth);
        getParentFragmentManager().setFragmentResult(AdminConstants.REQUEST_KEY,bundle);
    }
}
