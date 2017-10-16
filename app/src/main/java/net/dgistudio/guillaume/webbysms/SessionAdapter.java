package net.dgistudio.guillaume.webbysms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Guillaume on 12/01/2016.
 */


public class SessionAdapter extends ArrayAdapter<Sessions> {

    //tweets est la liste des models à afficher
    public SessionAdapter(Context context, List<Sessions> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_list_view,parent, false);
        }

        sessionViewHolder viewHolder = (sessionViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new sessionViewHolder();
            viewHolder.contactName = (TextView) convertView.findViewById(R.id.contactName);
            viewHolder.sessionName = (TextView) convertView.findViewById(R.id.sessionName);
            viewHolder.date = (TextView) convertView.findViewById(R.id.sessionDate);
            viewHolder.stat = (TextView) convertView.findViewById(R.id.statsUse);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Sessions session = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.contactName.setText(session.getContactName());
        viewHolder.sessionName.setText(session.getSessionName());
        viewHolder.stat.setText("rx:"+session.getRxSMS()+"/tx:"+session.getTxSMS());
        viewHolder.date.setText(session.getLastUse());

        return convertView;
    }

    public class sessionViewHolder {
        public TextView sessionName;
        public TextView contactName;
        public TextView date;
        public TextView stat;
    }
}

