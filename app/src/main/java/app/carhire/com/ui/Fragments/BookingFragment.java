package app.carhire.com.ui.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import app.carhire.com.BuildConfig;
import app.carhire.com.R;
import app.carhire.com.ui.ViewCars;

public class BookingFragment extends DialogFragment {
    private SharedPreferences sharedPref;
    private String prefFile = BuildConfig.APPLICATION_ID + ".PREFERENCE_FILE_KEY";
    private SharedPreferences.Editor editor;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.booking_dialog, null);
        EditText etAddress = content.findViewById(R.id.address);
        EditText etPhoneNumber= content.findViewById(R.id.phone_number);
        final String address = etAddress.getText().toString().trim();
        final String phoneNumber = etPhoneNumber.getText().toString().trim();
        sharedPref = getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);

        builder.setCancelable(false)
                .setMessage("We need these details to contact you in case you book a car")
                .setView(content)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editor = sharedPref.edit();
                        editor.putString("Address", address);
                        editor.putString("Phone number", phoneNumber);
                        editor.apply();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getContext(), ViewCars.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
