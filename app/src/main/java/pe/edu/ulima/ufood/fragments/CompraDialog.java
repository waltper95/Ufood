package pe.edu.ulima.ufood.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import pe.edu.ulima.ufood.R;
import pe.edu.ulima.ufood.bean.Item;
import pe.edu.ulima.ufood.interfaces.UFoodView;
import pe.edu.ulima.ufood.remote.Services;

public class CompraDialog extends DialogFragment implements View.OnClickListener{
    TextView tviNombre;
    TextView tviPrecio;
    TextView tviCalorias;
    TextView tviTarjetas;
    Button button;
    Item item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.tviNombre = (TextView)view.findViewById(R.id.nombre_compra);
        this.tviPrecio = (TextView)view.findViewById(R.id.precio_compra);
        this.tviCalorias = (TextView)view.findViewById(R.id.calorias_compra);
        this.tviTarjetas = (TextView)view.findViewById(R.id.tarjeta_compra);
        this.button = (Button)view.findViewById(R.id.but_compra);

        item = (Item)getArguments().getSerializable("item");
        NumberFormat format = new DecimalFormat("##0.00");
        if (item != null){
            tviNombre.setText(item.nombre);
            tviPrecio.setText(String.format("S/.%s", format.format(item.precio)));
            tviCalorias.setText(String.format("%s kcal", item.calorias));
        }
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        Bundle bundle = new Bundle();
        if(item != null){
            bundle.putSerializable("item", item);
        }
        Services.intentarPedido(item, (UFoodView)getContext());
    }
}
