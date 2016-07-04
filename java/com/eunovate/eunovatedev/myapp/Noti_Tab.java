package com.eunovate.eunovatedev.myapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.AttendanceInfoDBHelper;
import com.eunovate.eunovatedev.myapp.object.notiObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Noti_Tab extends Fragment {
    Timer timer;
    JSONObject jsonObj;
    JSONArray jsonArr;
    notiObject notiObj;
    private AttendanceDBHelper atd_db;
    private ArrayList<notiObject> noti_arr;
    List<Map<String, String>> myArrList;
    ListView myListView;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        myListView=(ListView) v.findViewById(R.id.notiList);
        atd_db=new AttendanceDBHelper(getActivity());
        pref=getActivity().getPreferences(0);
        myArrList=atd_db.get_noti_list();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), myArrList,android.R.layout.simple_list_item_2,new String[] {"description", "date"},
                new int[] {android.R.id.text1,android.R.id.text2});

        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //TODO Auto-generated method stub

            }
        });

        return v;
    }

}
