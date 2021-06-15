package com.example.homeutilitymerchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    final ArrayList<String> itemNameArray = new ArrayList<String>();
    final ArrayList<String> itemPriceArray = new ArrayList<String>();
    ListView listView;
    myAdapter adapter;
    ConstraintLayout failed;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        listView = view.findViewById(R.id.orderlistView);
        adapter = new myAdapter(getActivity(), itemNameArray, itemPriceArray);
        failed = view.findViewById(R.id.failedConstraint);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("order");
        query.whereEqualTo("merchantId", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("status", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                String objectId = null;
                String shopName = null;
                if (e == null && objects.size() > 0) {

                    for (ParseObject object : objects) {
                        objectId = object.getString("orderId");
                        itemNameArray.add(objectId);
                        Log.i("object", object.getString("orderId"));
                        shopName = object.getString("shopName");
                        itemPriceArray.add(shopName);

                    }
                    listView.setAdapter(adapter);


                    final Intent intent = new Intent(getActivity(), orderActivity.class);
                    intent.putExtra("shopName", shopName);
                    intent.putExtra("objectId", objectId);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "merchants not found", Toast.LENGTH_LONG).show();
                    failed.setVisibility(View.VISIBLE);

                }
            }
        });

        return view;
    }

    //adapter class
    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> merchantNameArray = new ArrayList<String>();
        ArrayList<String> descriptionArray = new ArrayList<String>();

        myAdapter(Context c, ArrayList<String> merchantNameArray, ArrayList<String> descriptionArray) {
            super(c, R.layout.order_custom_list, R.id.merchantNameTextView, merchantNameArray);
            this.context = c;
            this.merchantNameArray = merchantNameArray;
            this.descriptionArray = descriptionArray;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.order_custom_list, parent, false);
            TextView merchantName = item.findViewById(R.id.merchantNameTextView);
            TextView description = item.findViewById(R.id.descriptionTextView);

            merchantName.setText(merchantNameArray.get(position));
            description.setText(descriptionArray.get(position));
            Log.i("adapter set", "success");


            return item;
        }
    }
}
