package labs.course.modernartui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class ModernArtUI extends AppCompatActivity {

    private View tv1;
    private View tv2;
    private View tv3;
    private View tv4;
    private View tv5;
    public AlertDialogFragment mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern_art_ui);
        tv1 = findViewById(R.id.view1);
        tv2 = findViewById(R.id.view2);
        tv3 = findViewById(R.id.view3);
        tv4 = findViewById(R.id.view4);
        tv5 = findViewById(R.id.view5);
        tv1.setBackgroundColor(Color.argb(0xFF, 0xE0, 0xFF, 0));
        tv2.setBackgroundColor(Color.argb(0xDD, 0, 0, 0xE0));
        tv3.setBackgroundColor(Color.argb(0xFF, 0, 45, 0));
        tv4.setBackgroundColor(Color.argb(0xFF, 0xCC, 0xCC, 0xCC));
        tv5.setBackgroundColor(Color.argb(0xEE, 100, 0, 60));

        SeekBar sb = (SeekBar)findViewById(R.id.seek_bar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv1.setBackgroundColor(Color.argb(0xFF, 0xE0, 0xFF, progress));
                tv2.setBackgroundColor(Color.argb(0xDD, 0, progress, 0xEF));
                tv3.setBackgroundColor(Color.argb(0xFF, progress, 45, progress));
                tv4.setBackgroundColor(Color.argb(0xFF, 0xCC, 0xCC, 0xCC));
                tv5.setBackgroundColor(Color.argb(0xEE, 100, progress, 60));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modern_art_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.more_info) {
            showAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showAlertDialog(){
        mDialog = new AlertDialogFragment();
        mDialog.show(getFragmentManager(), "MOMA");
    }

    public static class AlertDialogFragment extends DialogFragment {

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            TextView tv = new TextView(getActivity().getApplicationContext());
            tv.setText("Inspired by the works of Artists such as\n" +
                    "Piet Mondrian and Ben Nicholson\n\n" +
                    "Click below to learn more!\n\n");
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK)
                    .setView(tv)

                    // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)

                     // Set up Yes Button
                    .setNegativeButton("Visit MOMA",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.moma.org"));
                                    if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                                        startActivity(intent);
                                }
                            })

                    // Set up No Button
                    .setPositiveButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dismiss();
                                }
                            }).create();
        }
    }
}
