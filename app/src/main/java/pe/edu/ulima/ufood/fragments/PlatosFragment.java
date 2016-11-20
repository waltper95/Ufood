package pe.edu.ulima.ufood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import pe.edu.ulima.ufood.R;
import pe.edu.ulima.ufood.bean.Item;
import pe.edu.ulima.ufood.interfaces.PlatosView;
import pe.edu.ulima.ufood.remote.Services;

public class PlatosFragment extends Fragment implements PlatosView{
    private RecyclerView recyclerView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView)inflater.inflate(R.layout.recycler_view, container, false);
        context = recyclerView.getContext();
        recyclerView.setAdapter(new ContentAdapter());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Services.obtenerPlatos(this);
    }

    @Override
    public void obtenerLista(List<Item> items) {
        ContentAdapter adapter = new ContentAdapter(items, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this.context)
                        .colorResId(R.color.line_color)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.line_left_margin, R.dimen.line_right_margin)
                        .build());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView avatar;
        public TextView name;
        public TextView description;
        private Context context;
        public Item item;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            this.context = context;

            avatar = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", item);

            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();

            CompraDialog compraDialog = new CompraDialog();
            compraDialog.setArguments(bundle);

            compraDialog.show(manager, "dialog_fragment");
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Item> items;
        private Item item;
        private Context context;

        public ContentAdapter(List<Item> items, Context context) {
            this.items = items;
            this.context = context;
        }

        public ContentAdapter() {
            this.items = new ArrayList<>();
        }

        @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, context);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            this.item = items.get(position);
            holder.item = item;
            holder.name.setText(item.nombre);
            holder.description.setText(String.format("%s kcal - S/.%s", item.calorias, item.precio));
            Picasso.with(this.context)
                    .load("https://ufood-dcee9.firebaseapp.com/img/Lomo-Saltado-730x430.png")
                    .resize(300, 300)
                    .centerCrop()
                    .error(R.drawable.ic_room_service)
                    .into(holder.avatar);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
