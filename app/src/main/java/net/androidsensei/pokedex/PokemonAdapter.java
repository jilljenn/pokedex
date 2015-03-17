package net.androidsensei.pokedex;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;

import net.androidsensei.pokedex.data.PokedexContract;

import java.security.acl.LastOwnerException;
import java.util.List;

public class PokemonAdapter extends CursorAdapter {

    public static final String TAG = PokemonAdapter.class
            .getSimpleName();

    private Context context;

    ImageLoader imageLoader = PokedexApplication.getInstance().getImageLoader();

    public PokemonAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        int idx_name = cursor.getColumnIndex(PokedexContract.PokemonEntry.COLUMN_NAME);
        int idx_description = cursor.getColumnIndex(PokedexContract.PokemonEntry.COLUMN_DESCRIPTION);
        String name = cursor.getString(idx_name);
        return name;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_pokemon, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // ViewHolder viewHolder = new ViewHolder();
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        boolean needToAdd = false;
        if(viewHolder == null) {
            needToAdd = true;
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.list_item_pokemon_textview);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.list_item_pokemon_imageview);
        }
        viewHolder.textView.setText(convertCursorRowToUXFormat(cursor));
        viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
        if(needToAdd)
            view.setTag(viewHolder);
    }

    static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }
}
