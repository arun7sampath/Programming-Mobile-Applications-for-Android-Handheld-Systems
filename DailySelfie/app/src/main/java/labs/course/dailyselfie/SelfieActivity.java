package labs.course.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SelfieActivity extends ListActivity {

    private int CAPTURE_IMAGE = 0x01;
    static final String TAG = "DailySelfie";
    static final String SELFIE_ARRAY = "Adapter-List";
    static final String SELFIE_FILES = "image-files";

    private static final long ALARM_PERIOD = 60 * 1000L;

    private SelfieAdapter mAdapter;
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListView SelfieListView = getListView();
        mAdapter = new SelfieAdapter(getApplicationContext());
        File files[];
        if(savedInstanceState != null){
            Log.i(TAG, "Saved Instance state is not null");
            files = (File [])savedInstanceState.getSerializable(SELFIE_FILES);
        }
        else {
            files = getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles();
        }

        for(File file : files){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
//            Bitmap image = setPic(file.getAbsolutePath());
            if(image == null){
                Log.i(TAG, "Image Decode is null");
            }
            SelfieData data = new SelfieData(file.getName(), file, image);
            mAdapter.add(data);
        }

        setListAdapter(mAdapter);
        setAlarm();

        SelfieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelfieData data = (SelfieData) mAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), SelfieEnlargeImage.class);
                intent.putExtra("FilePath", data.getFilePath().toString());
                startActivity(intent);
            }
        });

        SelfieListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.removeItem(position);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){

        if(requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK)
        {

            if(data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                Log.i(TAG, "data is not null");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                try {
                    // Create an image file name
                    mPhotoFile = createImageFile(timeStamp);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.i(TAG, "IOException: " + ex);
                }

                if(mPhotoFile != null) {

                    Log.i(TAG, "File location: " + mPhotoFile.getAbsolutePath());

                    writeImage(bitmap, mPhotoFile);

                    SelfieData record = new SelfieData(timeStamp + ".jpg", mPhotoFile, bitmap);
                    mAdapter.add(record);
                    Log.i(TAG, "ListView count: " + mAdapter.getCount());

                }
                else{
                    Log.i(TAG, "mPhotoFile is null");
                }

            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        File files[] = getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles();
        savedInstanceState.putSerializable(SELFIE_FILES, files);
//        Log.i(TAG, "Saving selfie files");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selfie, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.camera_icon) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_IMAGE);
            }
            else {
                Log.i(TAG, "No relevant activity found");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAlarm(){

        Intent mNotificationReceiverIntent;
        PendingIntent mNotificationReceiverPendingIntent;

        // Get the AlarmManager Service
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(SelfieActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                SelfieActivity.this, 0, mNotificationReceiverIntent, 0);

        // Set single alarm
        /*mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + ALARM_PERIOD,
                mNotificationReceiverPendingIntent);*/
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + ALARM_PERIOD, ALARM_PERIOD,
                mNotificationReceiverPendingIntent);

        Log.i(TAG, "Alarm Created");
    }

    private File createImageFile(String timeStamp) throws IOException {

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                timeStamp,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );

        File image = new File(storageDir, timeStamp + ".jpg");

        Log.i(TAG, "ImageFile: " + image.getAbsolutePath());

        return image;
    }

    public void writeImage(Bitmap bitmap, File PhotoFile){

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(PhotoFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null)
                    fos.close();
                fos = null;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
