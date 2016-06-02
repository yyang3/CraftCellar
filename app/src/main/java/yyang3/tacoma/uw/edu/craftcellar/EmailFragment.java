package yyang3.tacoma.uw.edu.craftcellar;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends DialogFragment {




    public EmailFragment() {
        // Required empty public constructor
    }



    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.fragment_email, null);
        final String t = "Dude I got a new Beverage\nBrand: " +
                getArguments().getString("Brand") + " \nTitle: "
                + getArguments().getString("Title") + "\nYear: "
                + getArguments().getInt("Year");
        final EditText mailto = (EditText)v.findViewById(R.id.mailto);
        final EditText subject = (EditText)v.findViewById(R.id.subject);
        final EditText text = (EditText)v.findViewById(R.id.email);
        text.setText(t);
        builder.setView(v)
        // Add action buttons
        .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // sign in the user ...
                String to = mailto.getText().toString();
                String s = subject.getText().toString();
                String email = text.getText().toString();

                Intent e = new Intent(Intent.ACTION_SEND);
                e.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                e.putExtra(Intent.EXTRA_SUBJECT, s);
                e.putExtra(Intent.EXTRA_TEXT, email);
                e.setType("message/rfc822");
                startActivity(Intent.createChooser(e, "Select Email Client"));
                dismiss();
                }
        }).setNegativeButton(R.string.edit_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }

}
