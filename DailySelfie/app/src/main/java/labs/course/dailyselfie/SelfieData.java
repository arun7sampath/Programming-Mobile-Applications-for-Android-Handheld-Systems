package labs.course.dailyselfie;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by Arun Sampath on 12/20/2015.
 */
public class SelfieData {

    private File mFilePath;
    private String mImageName;
    private Bitmap mImageBitmap;

    public SelfieData(String imageName, File filePath, Bitmap image){
        this.mImageName = imageName;
        this.mFilePath = filePath;
        this.mImageBitmap = image;
    }

    public String getImageName(){
        return mImageName;
    }

    public void setImageName(String imageName){
        this.mImageName = imageName;
    }

    public File getFilePath(){
        return mFilePath;
    }

    public void setFilePath(File filePath){
        this.mFilePath = filePath;
    }

    public Bitmap getImageBitmap(){
        return mImageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap){
        this.mImageBitmap = imageBitmap;
    }

    @Override
    public String toString(){
        return "ImageName: " + mImageName;

    }
}
