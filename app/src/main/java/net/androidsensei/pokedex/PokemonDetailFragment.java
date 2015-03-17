package net.androidsensei.pokedex;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;

import net.androidsensei.pokedex.data.PokedexContract;

public class PokemonDetailFragment extends Fragment {

    ImageLoader imageLoader = PokedexApplication.getInstance().getImageLoader();
    private static final String ARG_POKEMON = "pokemon";
    private Pokemon mPokemon;
    private boolean mIsTwoPaneLayout;
    private ShareActionProvider mShareActionProvider;

    public static PokemonDetailFragment newInstance(Pokemon mPokemon) {
        PokemonDetailFragment fragment = new PokemonDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POKEMON, mPokemon);
        fragment.setArguments(args);
        return fragment;
    }

    public PokemonDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPokemon = getArguments().getParcelable(ARG_POKEMON);
        }
        mIsTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_pokemon_detail,
                container, false);

        TextView nombre = (TextView)rootView.findViewById(R.id.pokemon_name);
        ImageView imagen = (ImageView)rootView.findViewById(R.id.pokemon_image);
        if(mPokemon!=null){

            if(mIsTwoPaneLayout){
                nombre.setText(mPokemon.getName());
                nombre.setVisibility(View.VISIBLE);
            }else{
                getActivity().setTitle(mPokemon.getName());
                nombre.setVisibility(View.INVISIBLE);
            }
            imagen.setImageResource(R.drawable.ic_launcher);
        }

        return rootView;
     }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pokemon_detail, menu);

        /* MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat
                .getActionProvider(item);
        mShareActionProvider.setShareIntent(getDefaultShareIntent()); */
        super.onCreateOptionsMenu(menu,inflater);
    }

    private Intent getDefaultShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (mPokemon != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, "¿Quien ese ese pokemon?");
            intent.putExtra(Intent.EXTRA_TEXT, mPokemon.getName());
        }
        return intent;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /* if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(getActivity());
            return true;
        }else if(id == R.id.action_ver_imagen){
        } */
        return super.onOptionsItemSelected(item);
    }
}
