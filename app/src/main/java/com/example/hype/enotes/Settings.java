package com.example.hype.enotes;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    static final int GALLERY_REQUEST = 1;
    ImageView imageView;
    EditText etName;
    EditText etStatus;

    static final String H_NAME = "H_NAME";
    static final String H_STATUS = "H_STATUS";
    static final String URI_IMG = "URI_IMG";

    Button btnSave;
    SharedPreferences sPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_settings, container, false);

        getActivity().setTitle("Настройки");

        //имя и статус
        etName = (EditText) v.findViewById(R.id.etHeadName);
        etStatus = (EditText) v.findViewById(R.id.etHeadStatus);

        //кнопка сохранения имени и статуса
        btnSave = (Button) v.findViewById(R.id.bttnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //записываем имя и статус в префер
                sPref = getActivity().getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(H_NAME, etName.getText().toString());
                ed.putString(H_STATUS, etStatus.getText().toString());
                ed.commit();
                ((MainActivity)getActivity()).setNameStatus();
                Toast.makeText(getContext(), "Сохранено", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
