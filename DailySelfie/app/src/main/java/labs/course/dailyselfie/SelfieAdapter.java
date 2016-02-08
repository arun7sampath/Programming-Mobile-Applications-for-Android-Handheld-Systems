package labs.course.dailyselfie;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arun Sampath on 12/20/2015.
 */
public class SelfieAdapter extends BaseAdapter {
    private ArrayList<SelfieData> list = new ArrayList<SelfieData>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public SelfieAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        ViewHolder holder;

        SelfieData curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater
                    .inflate(R.layout.selfie_list_view, parent, false);
            holder.selfie = (ImageView) newView.findViewById(R.id.selfie);
            holder.date = (TextView) newView.findViewById(R.id.selfie_date);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.selfie.setImageBitmap(curr.getImageBitmap());
        holder.date.setText(curr.getImageName());

        return newView;
    }

    static class ViewHolder {

        ImageView selfie;
        TextView date;

    }

    public void add(SelfieData listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<SelfieData> getList() {
        return list;
    }

    public void setList(ArrayList<SelfieData> al){
        list = al;
    }

    public void removeItem(int position){
        SelfieData data = (SelfieData)getItem(position);
        data.getFilePath().delete();
        list.remove(position);
        notifyDataSetChanged();
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }
}
