package com.example.homeutilitymerchant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class InfoFragment extends Fragment {
    TextView shopName;
    TextView merchantID;
    TextView name;
    TextView phone;
    TextView email;
    TextView occupation;
    TextView address;





// infalater
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info,container,false);
        shopName=view.findViewById(R.id.shopNameTextview);
        merchantID=view.findViewById(R.id.merchantIdTextView);
        name=view.findViewById(R.id.merchantINameTextView);
        phone=view.findViewById(R.id.merchantPhoneTextView);
        occupation=view.findViewById(R.id.merchantOccupation);
        email=view.findViewById(R.id.merchantEmailTextView);
        address=view.findViewById(R.id.merchantAddressTextView);
        Log.i("username",ParseUser.getCurrentUser().getUsername().toString());

        ParseQuery<ParseObject> query= ParseQuery.getQuery("merchantDetails");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0) {
                    for (ParseObject object : objects) {
                        String shop=object.getString("shopName");
                        Log.i("shop",shop);
                        shopName.setText(shop.toUpperCase());
                        merchantID.setText(object.getString("username"));
                        name.setText(object.getString("name"));
                        phone.setText(object.getString("phoneNumber"));
                        occupation.setText(object.getString("occupation"));
                        email.setText(object.getString("email"));
                        address.setText(object.getString("address"));

                    }
                }
                else {
                    Log.i("e",e.getMessage());
                }
            }
        });

        return view;
    }
}
