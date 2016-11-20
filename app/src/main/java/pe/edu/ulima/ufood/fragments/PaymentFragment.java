package pe.edu.ulima.ufood.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pe.edu.ulima.ufood.R;
import pe.edu.ulima.ufood.bean.Item;
import pe.edu.ulima.ufood.bean.Tarjeta;
import pe.edu.ulima.ufood.interfaces.UFoodView;
import pe.edu.ulima.ufood.remote.Services;

public class PaymentFragment extends Fragment implements View.OnClickListener{

    Button butAddTarjeta;
    EditText numTarjeta;
    EditText numCVV;
    EditText numMM;
    EditText numYYYY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_fragment, container, false);

        this.butAddTarjeta = (Button)view.findViewById(R.id.butAddTarjeta);
        this.numTarjeta = (EditText)view.findViewById(R.id.numTarjeta);
        this.numCVV = (EditText)view.findViewById(R.id.numCVV);
        this.numMM = (EditText)view.findViewById(R.id.numMM);
        this.numYYYY = (EditText)view.findViewById(R.id.numYYYY);
        this.butAddTarjeta.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Tarjeta tarjeta = new Tarjeta(
                Long.parseLong(numTarjeta.getText().toString()),
                Integer.parseInt(numMM.getText().toString()),
                Integer.parseInt(numYYYY.getText().toString()),
                Integer.parseInt(numCVV.getText().toString())
        );
        Item item = (Item)getArguments().getSerializable("item");
        Services.agregarTarjeta(tarjeta, item, (UFoodView)getActivity());
    }




}
