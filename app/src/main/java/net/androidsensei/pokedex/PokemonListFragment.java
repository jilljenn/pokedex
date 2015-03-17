package net.androidsensei.pokedex;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.androidsensei.pokedex.data.PokedexContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokemonListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final int POKEMON_LOADER = 0;

    private PokemonAdapter mPokemonAdapter;
    private ProgressBar progressBarLoading;
    private ListView listView;


    private Callbacks mCallbacks = sDummyCallbacks;

    public interface Callbacks {
        public void onItemSelected(Pokemon pokemon);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onItemSelected(Pokemon pokemon) {

        }
    };

    public PokemonListFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPokemonAdapter = new PokemonAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        progressBarLoading = (ProgressBar)rootView.findViewById(R.id.progressbar_loading);
        listView = (ListView) rootView.findViewById(R.id.listview_pokemon);

        listView.setAdapter(mPokemonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), PokemonDetailActivity.class)
                            .setData(PokedexContract.PokemonEntry.buildPokemonUri(cursor.getLong(0)));
                    startActivity(intent);
                }
                // mCallbacks.onItemSelected(pokemon);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(POKEMON_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        listView.setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            listView.setItemChecked(0, false);
        } else {
            listView.setItemChecked(position, true);
        }
    }


    public void runTask(){
        PokemonListApiTask apiTask = new PokemonListApiTask(getActivity(),
                listView,progressBarLoading);
        apiTask.execute();
    }

    private void updatePokedex() {
        PokemonListApiTask apiTask = new PokemonListApiTask(getActivity(),
                listView,progressBarLoading);
        apiTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updatePokedex();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = PokedexContract.PokemonEntry.COLUMN_NAME + " ASC";
        Uri pokemonUri = PokedexContract.PokemonEntry.CONTENT_URI;

        return new CursorLoader(getActivity(), pokemonUri, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mPokemonAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPokemonAdapter.swapCursor(null);
    }
}
