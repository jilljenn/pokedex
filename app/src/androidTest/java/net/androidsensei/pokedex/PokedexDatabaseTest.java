package net.androidsensei.pokedex;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import net.androidsensei.pokedex.data.PokedexContract;
import net.androidsensei.pokedex.data.PokedexDbHelper;
import net.androidsensei.pokedex.data.PokedexContract.PokemonEntry;
import net.androidsensei.pokedex.data.PokedexContract.LugarEntry;

public class PokedexDatabaseTest extends AndroidTestCase {

    public static final String LOG_TAG = PokedexDatabaseTest.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(PokedexDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new PokedexDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        int id = 1;
        String nombre = "Charizar";
        String avatar = "http://img2.wikia.nocookie.net/__cb20140203022724/p__/protagonist/images/9/95/Charizard.png";
        int numero = 5;
        double altura = 1.75;
        double peso = 220.25;

        ContentValues values = new ContentValues();
        values.put(PokemonEntry._ID, id);
        values.put(PokemonEntry.COLUMN_NOMBRE, nombre);
        values.put(PokemonEntry.COLUMN_AVATAR, avatar);
        values.put(PokemonEntry.COLUMN_NUMERO, numero);
        values.put(PokemonEntry.COLUMN_ALTURA, altura);
        values.put(PokemonEntry.COLUMN_PESO, peso);

        PokedexDbHelper dbHelper = new PokedexDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long pokemonId;
        pokemonId = db.insert(PokemonEntry.TABLE_NAME, null, values);

        assertEquals(id,pokemonId);

        String[] columns = {
                PokemonEntry._ID,
                PokemonEntry.COLUMN_NOMBRE,
                PokemonEntry.COLUMN_AVATAR,
                PokemonEntry.COLUMN_NUMERO,
                PokemonEntry.COLUMN_ALTURA,
                PokemonEntry.COLUMN_PESO,
        };

        Cursor cursor = db.query(
                PokemonEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (cursor.moveToFirst()) {
            int nombreIndex = cursor.getColumnIndex(PokemonEntry.COLUMN_NOMBRE);
            String nombrePokemon = cursor.getString(nombreIndex);

            assertEquals(nombre, nombrePokemon);
        }else {
            fail("Epic Fail :(");
        }

        cursor.close();
        dbHelper.close();
    }
}
