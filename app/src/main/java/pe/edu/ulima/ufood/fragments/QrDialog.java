package pe.edu.ulima.ufood.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pe.edu.ulima.ufood.R;

public class QrDialog extends DialogFragment{
    ImageView imageView;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        String codigo = getArguments().getString("codigo");

        imageView = (ImageView) view.findViewById(R.id.qr_dialog);
        textView = (TextView) view.findViewById(R.id.qr_code);
        textView.setText(codigo);
        Picasso.with(getContext())
                .load("https://api.qrserver.com/v1/create-qr-code/?size=1000x1000&data=" + codigo)
                .resize(800, 800)
                .error(R.drawable.ic_room_service)
                .into(imageView);
        return view;
    }
}
