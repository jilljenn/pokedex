package net.androidsensei.pokedex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.androidsensei.pokedex.data.PokedexContract.PokemonEntry;

/**
 * Created by jj on 12-10-14.
 */
public class PokedexDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pokedex.db";
    public static final int DATABASE_VERSION = 1;

    public PokedexDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_POKEMON_TABLE = "CREATE TABLE " + PokemonEntry.TABLE_NAME + " (" +
                PokemonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PokemonEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                PokemonEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_POKEMON_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PokemonEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
