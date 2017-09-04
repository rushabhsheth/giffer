package com.giffer.giffer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.giffer.giffer.NewsCard.NewsCard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

/**
 * Created by archanaarunkumar on 8/12/17.
 */

public class AddNewsActivity extends AppCompatActivity {

    private static final String TAG = "AddNewsActivity";

    private static final int RC_PHOTO_PICKER = 2;
    private static final int RC_CHANNEL_PICKER = 3;

    private CircleImageView mImagePickerButton;
    private ImageView mAddNewsImageView;

    private TextWatcher mTitleWatcher;
    private EditText mEditTitle;
    private TextView mAddNewsTitleLength;

    private TextWatcher mDescriptionWatcher;
    private EditText mEditDescription;
    private TextView mAddNewsDescriptionLength;
    private View mAddNewsSelectChannel;
    private CircleImageView mAddNewsSelectChannelLogo;
    private TextView mAddNewsSelectChannelName;
    private EditText mEditUrl;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mNewsDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mNewsPhotosStorageReference;


    private NewsCard mNewsCard;
    private Uri mSelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnews);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbaraddnews);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.activity_addnews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        mNewsCard = (NewsCard) intent.getSerializableExtra("NewsCard");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mNewsDatabaseReference = mFirebaseDatabase.getReference().child("news");
        mNewsPhotosStorageReference = mFirebaseStorage.getReference().child("news_photos");

        mAddNewsTitleLength = (TextView) findViewById(R.id.addnews_titlelength);
        mEditTitle = (EditText) findViewById(R.id.addnews_editTitle);
        mEditDescription = (EditText) findViewById(R.id.addnews_editDescription);
        mAddNewsDescriptionLength = (TextView) findViewById(R.id.addnews_descriptionlength);
        mImagePickerButton = (CircleImageView) findViewById(R.id.image_picker);
        mAddNewsImageView = (ImageView) findViewById(R.id.addNewsImageView);
        mAddNewsSelectChannel = (View) findViewById(R.id.addnews_selectchannel);
        mAddNewsSelectChannelLogo = (CircleImageView) findViewById(R.id.addnews_selectchannelLogo);
        mAddNewsSelectChannelName = (TextView) findViewById(R.id.addnews_selectchannelName);
        mEditUrl = (EditText) findViewById(R.id.addnews_editUrl);

        String imageName = "sample1";
        int id = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
        final String path = "android.resource://" + this.getPackageName() + "/" + id;
        Uri uri = Uri.parse(path);

        Glide.with(this)
                .load(uri)
                .into(mAddNewsImageView);


        // ImagePickerButton shows an image picker to upload a image for a message
        mImagePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Start new intent to select channel
        mAddNewsSelectChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewsActivity.this, SelectChannelActivity.class);
                startActivityForResult(intent, RC_CHANNEL_PICKER);
            }
        });

        mTitleWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                mAddNewsTitleLength.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        mEditTitle.addTextChangedListener(mTitleWatcher);

        mDescriptionWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                mAddNewsDescriptionLength.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        mEditDescription.addTextChangedListener(mDescriptionWatcher);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            mSelectedImageUri = data.getData();

            Glide.with(this)
                    .load(mSelectedImageUri)
                    .into(mAddNewsImageView);

        }
        else if (requestCode == RC_CHANNEL_PICKER ) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("channelName")) {
                    String channelName = data.getExtras().getString("channelName");
                    mAddNewsSelectChannelName.setText("g/"+channelName);
                    mAddNewsSelectChannelName.setTextColor(ContextCompat.getColor(this,R.color.gifTitle));
                }
                if(data.hasExtra("channelLogo")){
                    String channelLogo = data.getExtras().getString("channelLogo");

                    int id = this.getResources().getIdentifier(channelLogo, "mipmap", this.getPackageName());
                    mAddNewsSelectChannelLogo.setImageResource(id);
                }

            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addnews, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case (R.id.action_post):
                postNewsCard();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void postNewsCard() {

        // Get a reference to store file at chat_photos/<FILENAME>
        StorageReference newsPhotoRef = mNewsPhotosStorageReference.child(mSelectedImageUri.getLastPathSegment());

        // Upload file to Firebase Storage
        newsPhotoRef.putFile(mSelectedImageUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // When the image has successfully uploaded, we get its download URL
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mNewsCard.setNewsCardImageUri(downloadUrl.toString());

                        mNewsCard.setVideoTitle(mEditTitle.getText().toString());
                        mNewsCard.setVideoDescription(mEditDescription.getText().toString());
                        mNewsCard.setIdChannel(mAddNewsSelectChannelName.getText().toString());

                        Date currentTime = Calendar.getInstance().getTime();
                        mNewsCard.setTimePosted(currentTime.toString());

                        mNewsCard.setOriginalLink(mEditUrl.getText().toString());

                        mNewsDatabaseReference.push().setValue(mNewsCard);

                    }
                });
        Toast.makeText(this, "News added successfully", Toast.LENGTH_LONG).show();
    }

}


