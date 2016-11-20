package pe.edu.ulima.ufood.remote;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import pe.edu.ulima.ufood.bean.Item;
import pe.edu.ulima.ufood.bean.Pedido;
import pe.edu.ulima.ufood.bean.Tarjeta;
import pe.edu.ulima.ufood.interfaces.HistoriaView;
import pe.edu.ulima.ufood.interfaces.JugoDesayunoView;
import pe.edu.ulima.ufood.interfaces.NotificacionesView;
import pe.edu.ulima.ufood.interfaces.PlatosView;
import pe.edu.ulima.ufood.interfaces.UFoodView;

public class Services {
    private static FirebaseDatabase database;
    private static DatabaseReference ref;
    private static List<Item> items;
    public static String userUid;
    private static String keyPedido;

    public static void init() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(false);
        }
    }

    public static void obtenerPlatos(final PlatosView platosView) {
        ref = database.getReference("platos");
        items = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    items.add(data.getValue(Item.class));
                }
                platosView.obtenerLista(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Snackbar.make(coordinatorLayout, "onCancelled", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public static void obtenerJugoDesayunos(final JugoDesayunoView view) {
        items = new ArrayList<>();
        ref = database.getReference("jugos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren())
                    items.add(data.getValue(Item.class));
                obtenerSandwiches(view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("se cancelo");
            }
        });
    }

    private static void obtenerSandwiches(final JugoDesayunoView view) {
        ref = database.getReference("sandwiches");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int aux = items.size();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    items.add(data.getValue(Item.class));

                items.add(0, null);
                items.add(aux + 1, null);
                view.obtenerLista(items, aux);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("se cancelo");
            }
        });
    }

    public static void realizarPedido(Item item, final UFoodView view) {
        final Pedido pedido = new Pedido(
                new Date().getTime(),
                item.nombre,
                item.precio,
                false
        );

        ref = database.getReference();
        ref.child("users").child(Services.userUid).child("pedido")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null ||
                                dataSnapshot.child("listo").getValue(Boolean.class) ||
                                dataSnapshot.child("cancelado").getValue(Boolean.class)){

                            pedido.codigo = ref.getRoot().child("pedidos").push().getKey();
                            Services.keyPedido = pedido.codigo;

                            ref.getRoot().child("users").child(Services.userUid).child("pedido").setValue(pedido);

                            ref.getRoot().child("pedidos").child(pedido.codigo).setValue(pedido);
                            view.showNotificaciones();
                        }
                        else{
                            view.mostrarMensaje("Usted tiene pedidos en curso");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("serv. not.", databaseError.getDetails());
                    }
                });

    }


    public static void intentarPedido(final Item item, final UFoodView view) {
        ref = database.getReference();

        ref.child("users").child(Services.userUid).child("tarjetas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    realizarPedido(item, view);
                } else {
                    view.showPayment(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("serv. not.", databaseError.getDetails());
            }
        });
    }

    public static void setNotificaciones(final NotificacionesView view) {
        ref = database.getReference();
        ref = ref.child("users").child(Services.userUid).child("pedido").child("codigo");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String codigo = dataSnapshot.getValue(String.class);

                        ref.getRoot().child("pedidos").child(codigo).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Pedido pedido = dataSnapshot.getValue(Pedido.class);
                                ref.getRoot().child("users")
                                        .child(Services.userUid).child("pedido")
                                        .setValue(pedido);
                                if (pedido.listo)ref.getRoot().child("users").child(Services.userUid).child("historia").push().setValue(pedido);
                                Services.keyPedido = pedido.codigo;
                                view.setNotificacionesValues(pedido);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("serv. not.", databaseError.getDetails());
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("serv. not.", databaseError.getDetails());
                    }
        });
    }

    public static void cancelarPedido(){
        ref = database.getReference();
        ref = ref.child("pedidos").child(Services.keyPedido);
        ref.child("cancelacion").setValue(new Date().getTime());
        ref.child("cancelado").setValue(true);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pedido pedido = dataSnapshot.getValue(Pedido.class);
                ref.getRoot().child("users").child(Services.userUid).child("historia").push().setValue(pedido);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("serv. not.", databaseError.getDetails());
            }
        });
    }

    public static void agregarTarjeta(final Tarjeta tarjeta, final Item item, final UFoodView view){
        ref = database.getReference();
        ref.child("users").child(Services.userUid).child("tarjetas").push().setValue(tarjeta);
        Services.intentarPedido(item, view);
    }

    public static void obtenerHistoria(final HistoriaView view){
        ref = database.getReference();
        ref.child("users").child(Services.userUid).child("historia")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Pedido> pedidos = new ArrayList<>();
                        for (DataSnapshot data: dataSnapshot.getChildren())
                            pedidos.add(data.getValue(Pedido.class));

                        Collections.reverse(pedidos);
                        view.obtenerLista(pedidos);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("serv. not.", databaseError.getDetails());
                    }
                });
    }
}




