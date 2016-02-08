package labs.course.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Arun Sampath on 12/21/2015.
 */
public class SelfieEnlargeImage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfie_image_enlarged_view);

        Bundle extras = getIntent().getExtras();
        Bitmap bitmap;
        if(extras != null){
            bitmap = BitmapFactory.decodeFile(extras.getString("FilePath"));
            ImageView imageView = (ImageView)findViewById(R.id.enlarged_image);
            imageView.setImageBitmap(bitmap);
        }
        else {
            Log.i(SelfieActivity.TAG, "extras is null");
        }
    }
}