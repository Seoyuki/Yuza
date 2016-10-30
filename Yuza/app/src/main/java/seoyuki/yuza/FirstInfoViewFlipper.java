package seoyuki.yuza;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

/**
 * 뷰플리퍼를 위한 뷰
 *
 * @author Mike
 */
public class FirstInfoViewFlipper extends LinearLayout implements OnTouchListener {

    /**
     * Count of index buttons. Default is 3
     */
    public static int countIndexes = 3;

    /**
     * Button Layout
     */
    LinearLayout infoBtnLayout;

    /**
     * Index button images
     */
    ImageView[] indexButtons;

    /**
     * Views for the Flipper
     */
    View[] views;

    /**
     * Flipper instance
     */
    ViewFlipper infoFlipper;

    /**
     * X coordinate for touch down
     */
    float downX;

    /**
     * X coordinate for touch up
     */
    float upX;

    /**
     * Current index
     */
    int currentIndex = 0;


    public FirstInfoViewFlipper(Context context) {
        super(context);

        init(context);
    }

    public FirstInfoViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    /**
     * Initialize
     *
     * @param context
     */
    public void init(Context context) {
        setBackgroundColor(0x000000);

        // Layout inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.firstinfo_screenview, this, true);

        infoBtnLayout = (LinearLayout) findViewById(R.id.infoBtnLayout);
        infoFlipper = (ViewFlipper) findViewById(R.id.infoFlipper);
        infoFlipper.setOnTouchListener(this);


        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.leftMargin = 20;

        indexButtons = new ImageView[countIndexes];
        views = new ImageView[countIndexes];
        for(int i = 0; i < countIndexes; i++) {
            indexButtons[i] = new ImageView(context);

            if (i == currentIndex) {
                indexButtons[i].setImageResource(R.drawable.yuza_mini);
            } else {
                indexButtons[i].setImageResource(R.drawable.white);
            }

            indexButtons[i].setPadding(10, 10, 10, 10);
            infoBtnLayout.addView(indexButtons[i], params);

            ImageView curView = new ImageView(context);
            //ringBuilder imageString = new StringBuilder();
            //ageString.append("@drawable/test0");
            //ageString.append(i);
            //t resID = getResources().getIdentifier(imageString.toString(), "drawable", context.getPackageName());
            curView.setImageResource(R.drawable.firstinfo00+i);

            curView.setScaleType(ImageView.ScaleType.MATRIX);
            LayoutParams imageParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
            );
            curView.setBackgroundColor(0x000000);
            curView.setLayoutParams(imageParams);

            views[i] = curView;

            infoFlipper.addView(views[i]);
        }


    }

    /**
     * Update the display of index buttons
     */
    private void updateIndexes() {
        for(int i = 0; i < countIndexes; i++) {
            if (i == currentIndex) {
                indexButtons[i].setImageResource(R.drawable.yuza_mini);
            } else {
                indexButtons[i].setImageResource(R.drawable.white);
            }
        }
    }

    /**
     * onTouch event handling
     */
    public boolean onTouch(View v, MotionEvent event) {
        if(v != infoFlipper) return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            upX = event.getX();

            if( upX < downX ) {  // in case of right direction

                infoFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_left_in));
                infoFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_left_out));

                if (currentIndex < (countIndexes-1)) {
                    infoFlipper.showNext();

                    // update index buttons
                    currentIndex++;
                    updateIndexes();
                }
            } else if (upX > downX){ // in case of left direction

                infoFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_right_in));
                infoFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_right_out));

                if (currentIndex > 0) {
                    infoFlipper.showPrevious();

                    // update index buttons
                    currentIndex--;
                    updateIndexes();
                }
            }
        }

        return true;
    }

}
