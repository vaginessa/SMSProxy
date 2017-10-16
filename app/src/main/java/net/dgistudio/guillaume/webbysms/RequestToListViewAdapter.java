package net.dgistudio.guillaume.webbysms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Guillaume on 02/04/2016.
 */
public class RequestToListViewAdapter extends ArrayAdapter<Request> {


    public RequestToListViewAdapter(Context context, List<Request> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_request_layout, parent, false);
        }

        UserViewData viewHolder = (UserViewData) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new UserViewData();
            viewHolder.progressRequest = (ProgressBar) convertView.findViewById(R.id.progressRequest);
            viewHolder.requestText = (TextView) convertView.findViewById(R.id.requestSum);
            convertView.setTag(viewHolder);
        }

        Request request = getItem(position);


        viewHolder.requestText.setText(request.getMethod() + " : " + request.getUrl().substring(0, (request.getUrl().length() > 50) ? 50 : request.getUrl().length()));
        viewHolder.progressRequest.setProgress(100);

        return convertView;
    }

    public class UserViewData {

        public ProgressBar progressRequest;
        public TextView requestText;
    }
}
