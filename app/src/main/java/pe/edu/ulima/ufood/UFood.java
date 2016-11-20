package pe.edu.ulima.ufood;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import pe.edu.ulima.ufood.bean.Item;
import pe.edu.ulima.ufood.fragments.HistoriaFragment;
import pe.edu.ulima.ufood.fragments.JugosDesayunosFragment;
import pe.edu.ulima.ufood.fragments.NotificacionesFragment;
import pe.edu.ulima.ufood.fragments.PaymentFragment;
import pe.edu.ulima.ufood.fragments.PlatosFragment;
import pe.edu.ulima.ufood.fragments.TarjetaFragment;
import pe.edu.ulima.ufood.interfaces.UFoodView;
import pe.edu.ulima.ufood.remote.Services;

public class UFood extends AppCompatActivity implements UFoodView{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ufood);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            assert indicator != null;
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            actionBar.setHomeAsUpIndicator(indicator);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Services.init();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setChecked(true);
                        drawerLayout.closeDrawers();

                        switch (item.getItemId()){
                            case R.id.la_carta:
                                PlatosFragment platosFragment = new PlatosFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, platosFragment)
                                        .commit();
                                setTitle("La carta");
                                break;
                            case R.id.jugos_desayunos:
                                JugosDesayunosFragment jugosDesayunosFragment = new JugosDesayunosFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, jugosDesayunosFragment)
                                        .commit();
                                setTitle("Jugos y desayunos");
                                break;
                            case R.id.historia:
                                HistoriaFragment fragment = new HistoriaFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, fragment)
                                        .commit();
                                setTitle("Historia");
                                break;
                            case R.id.pago:
                                TarjetaFragment tarjetaFragment = new TarjetaFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, tarjetaFragment)
                                        .commit();
                                setTitle("Pago");
                                break;
                            case R.id.notificaciones:
                                NotificacionesFragment notificacionesFragment = new NotificacionesFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container,notificacionesFragment)
                                        .commit();
                                setTitle("Notificaciones");
                                break;
                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                break;
                        }
                        return true;
                    }
                }
        );

        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null) return;

            PlatosFragment platosFragment = new PlatosFragment();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, platosFragment).commit();
            setTitle("La carta");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPayment(Item item) {
        PaymentFragment paymentFragment = new PaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        paymentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, paymentFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showNotificaciones() {
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(5).setChecked(true);
        setTitle("Notificaciones");
        NotificacionesFragment paymentFragment = new NotificacionesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, paymentFragment)
                .commit();
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        Snackbar.make(
                findViewById(R.id.main_content),
                mensaje,
                Snackbar.LENGTH_LONG
        ).show();
    }
}
