package net.androidsensei.pokedex.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import net.androidsensei.pokedex.PokemonDetailActivity;
import net.androidsensei.pokedex.data.PokedexContract;
import net.androidsensei.pokedex.data.PokedexDbHelper;

import java.sql.SQLClientInfoException;

/**
 * Created by jj on 17/03/2015.
 */
public class PokemonProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PokedexDbHelper mOpenHelper;

    static final int POKEDEX = 1;
    static final int POKEMON = 151;

    private static final SQLiteQueryBuilder sPokemonQueryBuilder;

    static {
        sPokemonQueryBuilder = new SQLiteQueryBuilder();
    }

    private Cursor getPokemonById(Uri uri, String[] projection, String sortOrder) {
        String pokemonId = PokedexContract.PokemonEntry.getPokemonIdFromUri(uri);
        String selection = PokedexContract.PokemonEntry.TABLE_NAME + "." + PokedexContract.PokemonEntry._ID + " = ? ";
        return sPokemonQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                new String[]{pokemonId},
                null,
                null,
                sortOrder);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PokedexContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "pokedex", POKEDEX);
        matcher.addURI(authority, "pokedex/#", POKEMON);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PokedexDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return PokedexContract.PokemonEntry.CONTENT_TYPE;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // ""
            case POKEDEX: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PokedexContract.PokemonEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "/*"
            case POKEMON:
            {
                retCursor = getPokemonById(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long _id = db.insert(PokedexContract.PokemonEntry.TABLE_NAME, null, values);
        if (_id > 0)
            returnUri = PokedexContract.PokemonEntry.buildPokemonUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                try {
                    long _id = db.insert(PokedexContract.PokemonEntry.TABLE_NAME, null, value);
                    if (_id != -1) {
                        returnCount++;
                    }
                } catch (SQLiteConstraintException e) {
                    break;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}
