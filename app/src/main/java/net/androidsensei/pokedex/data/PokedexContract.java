package net.androidsensei.pokedex.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jj on 12-10-14.
 */
public class PokedexContract implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "net.androidsensei.pokedex.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class PokemonEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("pokedex").build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY;

        public static final String TABLE_NAME = "pokemon";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESCRIPTION = "description";

        public static Uri buildPokemonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getPokemonIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

}
