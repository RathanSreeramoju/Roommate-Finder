package example.abhiandroid.searchviewexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {



    Context mContext;
    LayoutInflater inflater;
    private List<roomsNames> roomsNamesList = null;
    private ArrayList<roomsNames> arraylist;

    public ListViewAdapter(Context context, List<roomsNames> roomsNamesList) {
        mContext = context;
        this.roomsNamesList = roomsNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<roomsNames>();
        this.arraylist.addAll(roomsNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return roomsNamesList.size();
    }

    @Override
    public roomsNames getItem(int position) {
        return roomsNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);

            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(roomsNamesListNamesList.get(position).getroomsName());
        return view;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        roomsNamesList.clear();
        if (charText.length() == 0) {
            roomsNamesList.addAll(arraylist);
        } else {
            for (roomsNamesListNames wp : arraylist) {
                if (wp.getroomsName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    roomsNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}