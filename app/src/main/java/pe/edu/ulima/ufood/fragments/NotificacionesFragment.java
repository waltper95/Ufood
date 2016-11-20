package pe.edu.ulima.ufood.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pe.edu.ulima.ufood.R;
import pe.edu.ulima.ufood.bean.Pedido;
import pe.edu.ulima.ufood.interfaces.NotificacionesView;
import pe.edu.ulima.ufood.remote.Services;

public class NotificacionesFragment extends Fragment implements NotificacionesView, View.OnClickListener{

    ImageView iviPedido;
    TextView tviNombre;
    TextView tviEstado;
    Button butCancelar;
    Button butVerCodigo;
    Pedido pedido;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificaciones, container, false);

        this.iviPedido = (ImageView)view.findViewById(R.id.notificaciones_img);
        this.tviNombre = (TextView)view.findViewById(R.id.notificaciones_nombre);
        this.tviEstado = (TextView)view.findViewById(R.id.notificaciones_estado);

        this.butCancelar = (Button)view.findViewById(R.id.but_cancelar);
        this.butVerCodigo = (Button)view.findViewById(R.id.but_ver_codigo);
        butVerCodigo.setOnClickListener(this);
        butCancelar.setOnClickListener(this);

        Picasso.with(getContext())
                .load("https://ufood-dcee9.firebaseapp.com/img/desayuno.png")
                .resize(300, 300)
                .centerCrop()
                .error(R.drawable.ic_room_service)
                .into(iviPedido);

        Services.setNotificaciones(this);
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void setNotificacionesValues(Pedido pedido) {
        this.pedido = pedido;
        tviNombre.setText(pedido.item);
        if (pedido.listo){
            tviEstado.setText("Listo");
            ViewGroup group = (ViewGroup)butCancelar.getParent();
            if (group != null) group.removeView(butCancelar);
        }
        else if(pedido.cancelado){
            tviEstado.setText("Cancelado");
            tviEstado.setTextColor(Color.RED);
            ViewGroup group = (ViewGroup)butCancelar.getParent();
            if (group != null) group.removeView(butCancelar);
        }
        else{
            tviEstado.setText("En curso");
        }
    }

    @Override
    public void onClick(View view) {
        Bundle bundle;
        if (view.getId() == R.id.but_ver_codigo){
            FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            QrDialog dialog = new QrDialog();
            bundle = new Bundle();
            bundle.putString("codigo", pedido.codigo);
            dialog.setArguments(bundle);
            dialog.show(manager, "qr_dialog");
        }
        else if(view.getId() == R.id.but_cancelar){
            Services.cancelarPedido();
        }
    }
}
