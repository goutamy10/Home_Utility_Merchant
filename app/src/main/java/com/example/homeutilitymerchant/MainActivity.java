package com.example.homeutilitymerchant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    String[] category = {"Select Your Profession", "Electrician", "Carpenter", "Painter"};
    String[] location = {"select Location", "Godadara", "Limbayat", "Dindoli", "Pandasara", "Sachin"};
    ConstraintLayout loginpageConstraintLayout;
    ConstraintLayout registrationConstraintLayout;
    Spinner registrationLocationSpinner;
    Spinner registrationMerchantSpinner;
    Spinner loginPageMerchantCategores;
    EditText firstName;
    EditText lastName;
    EditText shopName;
    EditText phoneNumber;
    EditText email;
    EditText password;
    EditText address;
    EditText loginPageUsername;
    EditText loginPagePassword;
    ParseObject merchantobj;
    EditText discription;
    Button liceneceImage;
    String check = null;
    TextView uploadText;
    TextView imageUpload;
    Button imageButton;

    // selecton form spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();


    }

    // spinner work when nothing selected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // already registered
    public void alreadyRegistred(View view) {
        ParseUser.logOut();
        firstName.setText(null);
        lastName.setText(null);
        password.setText(null);
        phoneNumber.setText(null);
        email.setText(null);
        address.setText(null);
        shopName.setText(null);
        liceneceImage.setEnabled(true);
        imageButton.setEnabled(true);
        imageUpload.setText(null);
        uploadText.setText(null);
        registrationConstraintLayout.setVisibility(View.INVISIBLE);
        loginpageConstraintLayout.setVisibility(View.VISIBLE);
    }

    //click here
    public void clickHere(View view) {
        ParseUser.logOut();
        loginPageUsername.setText(null);
        loginPagePassword.setText(null);
        loginpageConstraintLayout.setVisibility(View.INVISIBLE);
        registrationConstraintLayout.setVisibility(View.VISIBLE);
    }

    //email validator
    public static Boolean emailValidator(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();

    }

    private static String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    // submit
    //random number


    public void submitButtonMethod(View view) {
        ParseUser.logOut();
        Random rand = new Random();
        Integer number = rand.nextInt(1000);
        if (firstName.getText().toString().isEmpty() && lastName.getText().toString().isEmpty()
                && shopName.getText().toString().isEmpty() && phoneNumber.getText().toString().isEmpty() &&
                email.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
            firstName.setError("Please enter this section");
            firstName.requestFocus();
            lastName.setError("Please enter this section");
            lastName.requestFocus();
            shopName.setError("Please enter this section");
            shopName.requestFocus();
            phoneNumber.setError("Please enter this section");
            phoneNumber.requestFocus();
            address.setError("Please enter this section");
            address.requestFocus();
            password.setError("Please enter this section");
            password.requestFocus();
            email.setError("Please enter this section");
            email.requestFocus();


        } else if (uploadText.getText().toString().isEmpty()) {
            uploadText.setError("upload Image");
            uploadText.requestFocus();
        } else if (imageUpload.getText().toString().isEmpty()) {
            imageUpload.setError("upload Image");
            imageUpload.requestFocus();
        } else if (email.getText().toString().isEmpty() || emailValidator(email.getText().toString()) == false) {
            email.setError("please enter valid e-mail Id");
            email.requestFocus();
        } else if (password.getText().toString().length() < 6) {
            password.setError("Length should be at least 6");
            password.requestFocus();
        } else if (phoneNumber.length() != 10) {
            phoneNumber.setError("phone number should not be less than or more than 10");
            phoneNumber.requestFocus();
        } else if (registrationLocationSpinner.getSelectedItem().equals("select Location")) {
            Toast.makeText(getApplicationContext(), "Select Location in surat only", Toast.LENGTH_LONG).show();
        } else if (registrationMerchantSpinner.getSelectedItem().equals("Select Your Profession")) {
            Toast.makeText(getApplicationContext(), "Select Your job ", Toast.LENGTH_LONG).show();
        } else {


            Log.i("rand", String.valueOf(number));
            // parsing detail of merchant
            merchantobj = new ParseObject("merchantDetails");
            merchantobj.put("username", firstName.getText().toString() + number);
            merchantobj.put("name", firstName.getText().toString() + " " + lastName.getText().toString());
            merchantobj.put("occupation", registrationMerchantSpinner.getSelectedItem().toString());
            merchantobj.put("location", registrationLocationSpinner.getSelectedItem().toString());
            merchantobj.put("shopName", shopName.getText().toString());
            merchantobj.put("email", email.getText().toString());
            merchantobj.put("phoneNumber", phoneNumber.getText().toString());
            merchantobj.put("password", password.getText().toString());
            merchantobj.put("address", address.getText().toString());
            merchantobj.put("discription", discription.getText().toString());
            merchantobj.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("merchant object", "success");

                    } else {
                        Log.i("merchant object", "failed" + e.getMessage());
                    }
                }
            });
            // user sign up
            final ParseUser merchant = new ParseUser();
            merchant.setUsername(firstName.getText().toString() + number);
            merchant.setEmail(email.getText().toString());
            merchant.put("shopName", shopName.getText().toString());
            merchant.setPassword(password.getText().toString());
            merchant.put("name", firstName.getText().toString() + " " + lastName.getText().toString());
            merchant.put("phoneNumber", phoneNumber.getText().toString());
            merchant.put("occupation", registrationMerchantSpinner.getSelectedItem().toString());
            merchant.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("sign up ", "successful");
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                        loginpageConstraintLayout.setVisibility(View.VISIBLE);
                        registrationConstraintLayout.setVisibility(View.INVISIBLE);
                        ParseUser.logOut();
                    } else {
                        Log.i("sign up", "Failed " + e.getMessage());
                    }
                }
            });


        }


    }

    // Login
    public void loginButton(View view) {
        ParseUser.logOut();
        ParseUser user = new ParseUser();
        if (loginPageUsername.getText().toString().isEmpty()) {
            loginPageUsername.setError("Enter Youe merchant ID");
            loginPageUsername.requestFocus();
        } else if (loginPagePassword.getText().toString().isEmpty()) {
            loginPagePassword.setError("Enter Your Password");
            loginPagePassword.requestFocus();
        } else {
            user.getQuery().whereEqualTo("username", loginPageUsername.getText().toString()).findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects != null) {
                        for (ParseObject object : objects) {
                            check = object.getString("occupation");
                            Log.i("check", check);
                            String item = loginPageMerchantCategores.getSelectedItem().toString();
                            Log.i("item ", item);
                            if (check.equals(item)) {
                                Log.i("selected", loginPageMerchantCategores.getSelectedItem().toString());
                                ParseUser.logInInBackground(loginPageUsername.getText().toString(), loginPagePassword.getText().toString(), new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "select your occupation correct", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                }
            });


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //access
        loginpageConstraintLayout = findViewById(R.id.loginPageConstraintLayout);
        registrationConstraintLayout = findViewById(R.id.registrationConstraintLayout);
        liceneceImage = findViewById(R.id.licienceButton);
        uploadText = findViewById(R.id.uploadText);
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        shopName = findViewById(R.id.shopNameEditText);
        phoneNumber = findViewById(R.id.phoneEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        address = findViewById(R.id.addressEditText);
        discription = findViewById(R.id.descriptionEditText);
        loginPagePassword = findViewById(R.id.loginPagePasswordEditText);
        loginPageUsername = findViewById(R.id.loginPageUserNameEditText);
        imageUpload = findViewById(R.id.imageUpload);
        imageButton = findViewById(R.id.imageButton);
        //keyboard management
        registrationConstraintLayout.setOnClickListener(this);

        loginpageConstraintLayout.setVisibility(View.VISIBLE);
        registrationConstraintLayout.setVisibility(View.INVISIBLE);
        // Parse server connecter
        // ip =http://3.12.83.230:80/parse
        //id =  user
        // password = NzSpGagSm662
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("23f0b09424fcfa66573be436b1a8bebe57d915db")
                .clientKey("f3fdc0e183d71394ccca320fb42168735f1d1590")
                .server("http://3.12.83.230:80/parse/")
                .build()
        );


        setTitle("Merchant");
        //log in maagement
        if (ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
        //random number for user

        // spinner setting of merchant category in login constraint
        loginPageMerchantCategores = findViewById(R.id.loginPageMerchantCategoriesSpinner);
        loginPageMerchantCategores.setOnItemSelectedListener(this);
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loginPageMerchantCategores.setAdapter(categoryArrayAdapter);
        //spinner's Registration
        //merchant category
        registrationMerchantSpinner = findViewById(R.id.registrationMerchantSpinner);
        registrationMerchantSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> registrationMerchantArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        registrationMerchantArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registrationMerchantSpinner.setAdapter(registrationMerchantArrayAdapter);
        //location
        registrationLocationSpinner = findViewById(R.id.registrationLocationSpinner);
        registrationLocationSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> locationArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location);
        locationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registrationLocationSpinner.setAdapter(locationArrayAdapter);

        // licience image uploading

        liceneceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Upload Licience")
                        .setIcon(android.R.drawable.ic_menu_gallery)
                        .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //upload image code
                                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                } else {
                                    getPhoto();
                                }
                            }
                        })
                        .setNegativeButton("Dismiss", null)
                        .show();

            }
        });
        //icon image
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Upload image")
                        .setMessage("icon of your shop or work")
                        .setIcon(android.R.drawable.ic_menu_gallery)
                        .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //upload image code
                                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                } else {
                                    imagePhoto();
                                }
                            }
                        })
                        .setNegativeButton("Dismiss", null)
                        .show();

            }
        });

    }

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void imagePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    //permission for access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        } else if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imagePhoto();
            }
        }
    }

    byte[] byteArray;

    ByteArrayOutputStream stream;

    //selecting image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = data.getData();
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Log.i("licience image", "success");
                stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

                final ParseFile file = new ParseFile(firstName.getText().toString() + ".jpeg", byteArray);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseObject iupload = new ParseObject("merchantLicience");
                            iupload.put("shopName", shopName.getText().toString());
                            iupload.put("userName", firstName.getText().toString() + " " + lastName.getText().toString());
                            iupload.put("licience", file);
                            iupload.saveInBackground();
                            Log.i("upload ", "success");
                            uploadText.setText("Upload success");
                            liceneceImage.setEnabled(false);
                        } else {
                            // Failed
                            Log.i("did it fail?", "yes" + e.getMessage());
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Log.i("special image", "success");
                stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

                final ParseFile file = new ParseFile(firstName.getText().toString() + ".jpeg", byteArray);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseObject iupload = new ParseObject("merchantIcon");
                            iupload.put("shopName", shopName.getText().toString());
                            iupload.put("userName", firstName.getText().toString() + " " + lastName.getText().toString());
                            iupload.put("Icon", file);
                            iupload.saveInBackground();
                            imageUpload.setText("Upload success");
                            imageButton.setEnabled(false);
                            Log.i("image upload ", "success");
                        } else {
                            // Failed
                            Log.i("did it fail?", "yes" + e.getMessage());
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    //keyboard
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registrationConstraintLayout || v.getId() == R.id.loginPageConstraintLayout) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
