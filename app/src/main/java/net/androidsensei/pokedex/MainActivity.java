package net.androidsensei.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements
        PokemonListFragment.Callbacks{

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    private Pokemon mCurrentPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.e(LOG_TAG, "Oh hey");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        if (findViewById(R.id.pokemon_detail_container) != null) {
            mTwoPane = true;

            ((PokemonListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.pokemon_list))
                    .setActivateOnItemClick(true);
        }

        if (savedInstanceState == null && mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pokemon_detail_container, new PokemonDetailFragment(), DETAILFRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(Pokemon pokemon) {
        mCurrentPokemon = pokemon;
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pokemon_detail_container, new PokemonDetailFragment(), DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, PokemonDetailActivity.class);
            detailIntent.putExtra("pokemon", pokemon);
            startActivity(detailIntent);
        }
    }
}
