package pe.edu.ulima.ufood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pe.edu.ulima.ufood.R;
import pe.edu.ulima.ufood.bean.Pedido;
import pe.edu.ulima.ufood.interfaces.HistoriaView;
import pe.edu.ulima.ufood.remote.Services;

public class HistoriaFragment extends Fragment implements HistoriaView{
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView)inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setAdapter(new Adapter());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Services.obtenerHistoria(this);
    }

    @Override
    public void obtenerLista(List<Pedido> pedidos) {
        Adapter adapter = new Adapter(pedidos, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .colorResId(R.color.line_color)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.line_left_margin, R.dimen.line_right_margin)
                        .build());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iviAvatar;
        TextView tviTitle;
        TextView tviDesc;
        TextView tviFecha;
        TextView tviPrecio;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super (inflater.inflate(R.layout.item_historia, parent, false));

            iviAvatar = (ImageView)itemView.findViewById(R.id.hist_list_avatar);
            tviTitle = (TextView)itemView.findViewById(R.id.hist_list_title);
            tviDesc = (TextView)itemView.findViewById(R.id.hist_list_desc);
            tviFecha = (TextView)itemView.findViewById(R.id.hist_list_fecha);
            tviPrecio = (TextView)itemView.findViewById(R.id.hist_list_price);
        }
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Pedido> pedidos;
        private Context context;

        public Adapter() {
            pedidos = new ArrayList<>();
        }

        public Adapter(List<Pedido> pedidos, Context context) {
            this.pedidos = pedidos;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Pedido pedido = pedidos.get(position);
            holder.tviTitle.setText(pedido.item);
            holder.tviPrecio.setText(String.format("S/.%s", pedido.precio));

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy 'at' h:mm a");
            String desc = format.format(pedido.fecha);
            holder.tviFecha.setText(desc);

            if (pedido.listo){
                desc = "Recogido ";
            }
            else if (pedido.cancelado){
                desc = "Cancelado ";
            }
            holder.tviDesc.setText(desc);

            Picasso.with(this.context)
                    .load("https://ufood-dcee9.firebaseapp.com/img/Lomo-Saltado-730x430.png")
                    .resize(300,300)
                    .centerCrop()
                    .error(R.drawable.ic_room_service)
                    .into(holder.iviAvatar);
        }

        @Override
        public int getItemCount() {
            return pedidos.size();
        }
    }
}
