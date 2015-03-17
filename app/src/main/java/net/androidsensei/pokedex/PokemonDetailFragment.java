package net.androidsensei.pokedex;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
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

public class PokemonDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = PokemonDetailFragment.class.getSimpleName();

    ImageLoader imageLoader = PokedexApplication.getInstance().getImageLoader();
    private static final String ARG_POKEMON = "pokemon";
    private Pokemon mPokemon;
    private String pokemonInfo;
    private boolean mIsTwoPaneLayout;
    private ShareActionProvider mShareActionProvider;

    private static final int DETAIL_LOADER = 0;

    private static final String[] POKEMON_COLUMNS = {
            PokedexContract.PokemonEntry.TABLE_NAME + "." + PokedexContract.PokemonEntry._ID,
            PokedexContract.PokemonEntry.COLUMN_NAME,
            PokedexContract.PokemonEntry.COLUMN_DESCRIPTION
    };

    private static final int COL_POKEMON_ID = 0;
    private static final int COL_POKEMON_NAME = 1;
    private static final int COL_POKEMON_DESCRIPTION = 2;

    public PokemonDetailFragment() {
        setHasOptionsMenu(true);
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

        /* setHasOptionsMenu(true);

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
        } */

        return inflater.inflate(R.layout.fragment_pokemon_detail, container, false);

        // return rootView;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                POKEMON_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "data");
        if (!data.moveToFirst()) { return; }
        String name = data.getString(COL_POKEMON_NAME);
        String description = data.getString(COL_POKEMON_DESCRIPTION);
        pokemonInfo = String.format("%s - %s", name, description);
        TextView detailTextView = (TextView) getView().findViewById(R.id.pokemon_name);
        detailTextView.setText(pokemonInfo);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
