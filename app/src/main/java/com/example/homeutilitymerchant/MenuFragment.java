package com.example.homeutilitymerchant;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    Button addItem;
    ListView itemListView;
    EditText item;
    EditText price;
    myAdapter adapter;

    final ArrayList<String> itemNameArray = new ArrayList<String>();
    final ArrayList<String> itemPriceArray=new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_menu,container,false);
        addItem=view.findViewById(R.id.addItemButton);
        itemListView=view.findViewById(R.id.itemListView);
        adapter=new myAdapter(getActivity(), itemNameArray, itemPriceArray);


        // add item buttom
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alet=new AlertDialog.Builder(getActivity());
                View mview=getLayoutInflater().inflate(R.layout.item_dialougue,null);
                Button ok=mview.findViewById(R.id.ok);
                Button cancel=mview.findViewById(R.id.cancell);

                 item=mview.findViewById(R.id.itemEditText);
                 price=mview.findViewById(R.id.itemPriceEditText);

                alet.setView(mview);
                final AlertDialog alertDialog=alet.create();
                alertDialog.setCanceledOnTouchOutside(false);
                //ok button
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("item",item.getText().toString());
                        Log.i("price",price.getText().toString());

                        ParseObject itemObject=new ParseObject(ParseUser.getCurrentUser().getUsername()+"Item");
                        itemObject.put("itemName",item.getText().toString());
                        itemNameArray.add(item.getText().toString());
                        itemObject.put("itemPrice",price.getText().toString());
                        itemPriceArray.add(price.getText().toString());
                        itemListView.setAdapter(adapter);
                        alertDialog.dismiss();
                        itemObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){
                                    Toast.makeText(getActivity(), item.getText().toString(), Toast.LENGTH_LONG).show();
                                    ParseQuery<ParseObject> query1=ParseQuery.getQuery(ParseUser.getCurrentUser()+"Item");
                                    query1.whereEqualTo("itemName",item.getText());
                                    query1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e==null && objects.size()>0){
                                                for (ParseObject object:objects){
                                                    itemNameArray.add(object.getString("itemName"));
                                                    itemPriceArray.add(object.getString("itemPrice"));

                                                }
                                            }
                                        }
                                    });


                                }
                                else {
                                    Toast.makeText(getActivity(), "failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });



                    }
                });
                    // cancel button
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });




                alertDialog.show();

                itemListView.setAdapter(adapter);


            }
        });

        final ParseQuery<ParseObject> query=ParseQuery.getQuery(ParseUser.getCurrentUser().getUsername()+"Item");
// item added in array list
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0) {
                    for (ParseObject object : objects) {

                        Log.i("iteam name",object.getString("itemName"));
                        itemNameArray.add(object.getString("itemName"));

                        Log.i("item price",object.getString("itemPrice"));
                        itemPriceArray.add(object.getString("itemPrice"));
                    }
                    for (int i=0;i<itemNameArray.size();i++){
                        Log.i("itemArray",itemNameArray.get(i));
                    }
                    for (String a:itemPriceArray){
                        Log.i("item price array",a);
                    }
                    // adapter for listView

                    itemListView.setAdapter(adapter);


                    itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                            Toast.makeText(getActivity(), itemNameArray.get(position), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder deletedialog=new AlertDialog.Builder(getActivity())
                                    .setTitle("Delete item")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            final ParseQuery<ParseObject> deletequery=ParseQuery.getQuery(ParseUser.getCurrentUser().getUsername()+"Item");
                                            deletequery.whereEqualTo("itemName",itemNameArray.get(position));
                                            deletequery.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> objects, ParseException e) {
                                                    if (e==null && objects.size()>0){
                                                        for (ParseObject object:objects){
                                                            object.remove("itemName");
                                                            object.deleteInBackground(new DeleteCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    if (e==null){
                                                                        Log.i("delete","success");

                                                                    }else {
                                                                        Log.i("delete ","failed");
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }else {
                                                        Log.i("query ","failed "+e.getMessage());
                                                    }
                                                }
                                            });
                                            itemNameArray.remove(position);
                                            itemPriceArray.remove(position);
                                            itemListView.setAdapter(adapter);


                                        }
                                    })
                                    .setNegativeButton("cancel",null);


                                deletedialog.show();
                            return true;
                        }
                    });

                }else {
                    Toast.makeText(getActivity(), "add item", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }


    //adapter class
    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> itemNameArray = new ArrayList<String>();
         ArrayList<String> itemPriceArray=new ArrayList<String>();

        myAdapter(Context c, ArrayList<String> itemNameArray, ArrayList<String> itemPriceArray){
            super( c,R.layout.custom_listview,R.id.itemTextView,itemNameArray);
            this.context=  c;
            this.itemNameArray=itemNameArray;
            this.itemPriceArray=itemPriceArray;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View item=layoutInflater.inflate(R.layout.custom_listview,parent,false);
            TextView mitem=item.findViewById(R.id.itemTextView);
            TextView mprice=item.findViewById(R.id.priceTextView);

            mitem.setText(itemNameArray.get(position));
            mprice.setText(itemPriceArray.get(position));
            Log.i("adapter set","success");


            return item;
        }
    }




}
