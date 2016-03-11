package com.example.bricenangue.timeme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyEvent extends Fragment implements AdapterView.OnItemClickListener {


    private ListView lv_android;
    private AndroidListAdapter list_adapter;
    private MySQLiteHelper mySQLiteHelper;
    private UserLocalStore userLocalStore;

    public FragmentMyEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        CalendarCollection.date_collection_arr=new ArrayList<>();
        userLocalStore=new UserLocalStore(getContext());
        mySQLiteHelper=new MySQLiteHelper(getContext());

        getEvents(mySQLiteHelper.getAllIncomingNotification());
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_my_event, container, false);
        TextView tv=(TextView)rootView.findViewById(R.id.section_label);
        lv_android = (ListView) rootView.findViewById(R.id.myeventslistview);
        list_adapter=new AndroidListAdapter(getContext(),R.layout.list_item, CalendarCollection.date_collection_arr);
        lv_android.setAdapter(list_adapter);
        lv_android.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), "You have event on this date: " + CalendarCollection.date_collection_arr.get(position).datetime +
                CalendarCollection.date_collection_arr.get(position).title, Toast.LENGTH_LONG).show();
    }
    private void getEvents(ArrayList<IncomingNotification> incomingNotifications){

        for (int i=0;i<incomingNotifications.size();i++){
            JSONObject jo_inside = null;
            try {
                jo_inside = new JSONObject(incomingNotifications.get(i).body);

                String titel = jo_inside.getString("title");
                String infotext = jo_inside.getString("description");
                String creator = jo_inside.getString("creator");
                String creationTime = jo_inside.getString("datetime");
                String category = jo_inside.getString("category");
                String startingtime = jo_inside.getString("startingtime");
                String endingtime = jo_inside.getString("endingtime");
                String alldayevent = jo_inside.getString("alldayevent");
                String eventHash = jo_inside.getString("hashid");

                CalendarCollection  object =new CalendarCollection(titel,infotext,creator,creationTime,startingtime,endingtime,eventHash,category,alldayevent);

                if(object.creator.contains(userLocalStore.getLoggedInUser().getfullname())){
                    CalendarCollection.date_collection_arr.add(object);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
