package pe.edu.ulima.ufood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import pe.edu.ulima.ufood.interfaces.JugoDesayunoView;
import pe.edu.ulima.ufood.remote.Services;

public class JugosDesayunosFragment extends Fragment implements JugoDesayunoView{
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
        Services.obtenerJugoDesayunos(this);
    }

    @Override
    public void obtenerLista(List<Item> items, int division) {
        ContentAdapter adapter = new ContentAdapter(items, context, division);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this.context)
                        .colorResId(R.color.line_color)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.line_left_margin, R.dimen.line_right_margin)
                        .build());
    }

    public static class MViewHolder extends RecyclerView.ViewHolder{
        public MViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolder extends MViewHolder implements View.OnClickListener{
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

    public static class CategoryHolder extends MViewHolder{
        public TextView category;

        public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super (inflater.inflate(R.layout.category_list, parent, false));
            category = (TextView)itemView.findViewById(R.id.category);
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<MViewHolder> {
        private List<Item> items;
        private Item item;
        private Context context;
        private int division;

        public ContentAdapter(List<Item> items, Context context, int division) {
            this.items = items;
            this.context = context;
            this.division = division + 1;
        }

        public ContentAdapter() {
            this.items = new ArrayList<>();
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0 || viewType == division){
                return new CategoryHolder(LayoutInflater.from(parent.getContext()), parent);
            }else {
                return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, context);
            }
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            if (holder.getItemViewType() == 0){
                CategoryHolder categoryHolder = (CategoryHolder)holder;
                categoryHolder.category.setText("Jugos");
            }
            else if (holder.getItemViewType() == division){
                CategoryHolder categoryHolder = (CategoryHolder)holder;
                categoryHolder.category.setText("Sandwiches");
            }
            else{
                this.item = items.get(position);
                ViewHolder viewHolder = (ViewHolder)holder;
                viewHolder.item = item;
                viewHolder.name.setText(item.nombre);
                viewHolder.description.setText(String.format("%s kcal - S/.%s", item.calorias, item.precio));
                Picasso.with(this.context)
                        .load("https://ufood-dcee9.firebaseapp.com/img/desayuno.png")
                        .resize(300, 300)
                        .centerCrop()
                        .error(R.drawable.ic_room_service)
                        .into(viewHolder.avatar);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
