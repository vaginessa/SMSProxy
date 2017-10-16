package net.dgistudio.guillaume.webbysms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class NewSession extends AppCompatActivity {

    private SqlDbAccess dbAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Create new session");
        dbAccess = new SqlDbAccess(this);
        dbAccess.open();

        setContentView(R.layout.activity_new_session);
        Button btnNewSession = (Button)findViewById(R.id.btnStartSession);
        Spinner contactChooser = (Spinner)findViewById(R.id.spinner);

        List<String> contacts = new ArrayList<>();
        //TODO: Check for permission befor trying to access contacts

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        assert phones != null;
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (!phoneNumber.matches("[a-zA-Z]*"))
            {
                String item = name + " <" +phoneNumber.replaceAll(" ", "")+'>';

                contacts.add(item);
            }
            else {
                Log.d("NewSession", "Not Valid Number : "+phoneNumber);
            }


        }
        phones.close();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contacts);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        contactChooser.setAdapter(dataAdapter);



        btnNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText sessionName = (EditText)findViewById(R.id.ipt_SessionName);
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                if (sessionName.getText().toString().equals(""))
                {
                    Snackbar.make(v, "Session name can't be empty !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                String currentSpinnerItem = spinner.getSelectedItem().toString();
                String contactName = currentSpinnerItem.substring(0, currentSpinnerItem.indexOf('<'));
                String contactNumber = currentSpinnerItem.substring(currentSpinnerItem.indexOf('<')).replaceAll("<", "").replaceAll(">", "").replaceAll(" ", "");
                Switch sw = (Switch)findViewById(R.id.sw_hide_msg);

                Sessions session = dbAccess.createNewSession(sessionName.getText().toString(), contactName, contactNumber, sw.isChecked());

                Intent i = new Intent(NewSession.this, ClientWebViewActivity.class);
                i.setAction("websms.action.loadNew");
                i.putExtra("sessionId", session.getId());
                startActivity(i);
            }
        });
    }
}
