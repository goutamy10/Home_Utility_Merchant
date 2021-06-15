package com.example.homeutilitymerchant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class orderActivity extends AppCompatActivity {
    List<String> price = new ArrayList<>();
    List<String> name = new ArrayList<>();
    final ArrayList<String> itemNameArray = new ArrayList<String>();
    final ArrayList<String> itemPriceArray = new ArrayList<String>();
    Button moreButton;
    myAdapter adapter;
    Integer total = 300;
    ListView orderActivityListView;
    TextView totalTextView;
    Button completeButton;
    String objectId;
    ConstraintLayout mainConstraint;
    ConstraintLayout completedConstraint;
    TextView nameTextView;
    TextView addressTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderActivityListView = findViewById(R.id.orderActivityListView);
        moreButton = findViewById(R.id.moreButtom);
        completeButton = findViewById(R.id.completeButton);
        nameTextView=findViewById(R.id.nameTextView);
        addressTextView=findViewById(R.id.addressTextView);
        mainConstraint = findViewById(R.id.mainConstraint);
        mainConstraint.setVisibility(View.VISIBLE);
        completedConstraint = findViewById(R.id.completedConstraint);
        completedConstraint.setVisibility(View.INVISIBLE);
        adapter = new myAdapter(getApplicationContext(), itemNameArray, itemPriceArray);
        Intent a = getIntent();
        totalTextView = findViewById(R.id.totalTextView);
        String shopName = a.getStringExtra("shopName");
        objectId = a.getStringExtra("objectId");
        Log.i("object", objectId);
        Log.i("shopname", shopName);
        ParseQuery<ParseObject> objectParseQuery = ParseQuery.getQuery("order");
        objectParseQuery.whereEqualTo("orderId", objectId);
        objectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        nameTextView.setText(object.getString("name"));
                        addressTextView.setText(object.getString("address"));
                        Log.i("merchant", object.getString("merchantId"));
                        String p = String.valueOf(object.getList("itemPrice"));
                        String n = String.valueOf(object.getList("itemName"));
                        String splitPrice[] = p.split("\\[|\\]|,");
                        String splitName[] = n.split("\\[|\\]|,");
                        price = Arrays.asList(splitPrice);
                        name = Arrays.asList(splitName);
                        for (String s : price) {
                            Log.i("price", s);
                            itemPriceArray.add(s);

                        }
                        for (String r : name) {
                            Log.i("name", r);
                            itemNameArray.add(r);

                        }

                        itemNameArray.remove(0);
                        itemPriceArray.remove(0);


                        orderActivityListView.setAdapter(adapter);
                        totalTextView.setText(String.valueOf(object.getInt("total")));
                    }
                    while (itemPriceArray.contains("") && itemNameArray.contains("")) {
                        itemNameArray.remove("");
                        itemPriceArray.remove("");
                    }


                } else {
                    Log.i("error", "occurs");
                }
            }
        });
        // alert dialogue

    }


    // complete button
    public void setCompleteButton(View view) {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        final ParseQuery<ParseObject> query1 = ParseQuery.getQuery("order");
        query1.whereEqualTo("orderId", objectId);
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        object.put("status", true);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(orderActivity.this, "order completed", Toast.LENGTH_SHORT).show();
                                    completedConstraint.setVisibility(View.VISIBLE);
                                    mainConstraint.setVisibility(View.INVISIBLE);
                                } else {
                                    Toast.makeText(orderActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //adapter class
    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> itemNameArray = new ArrayList<String>();
        ArrayList<String> itemPriceArray = new ArrayList<String>();

        myAdapter(Context c, ArrayList<String> itemNameArray, ArrayList<String> itemPriceArray) {
            super(c, R.layout.custom_listview, R.id.itemTextView, itemNameArray);
            this.context = c;
            this.itemNameArray = itemNameArray;
            this.itemPriceArray = itemPriceArray;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.custom_listview, parent, false);
            TextView mitem = item.findViewById(R.id.itemTextView);
            TextView mprice = item.findViewById(R.id.priceTextView);

            mitem.setText(itemNameArray.get(position));
            mprice.setText(itemPriceArray.get(position));
            Log.i("adapter set", "success");


            return item;
        }
    }


}
