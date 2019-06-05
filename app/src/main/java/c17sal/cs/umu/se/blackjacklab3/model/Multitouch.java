package c17sal.cs.umu.se.blackjacklab3.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class Multitouch extends View
{
    public Multitouch(Context context) {
        this(context, null, 0);
    }

    public Multitouch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Multitouch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();



        switch (action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
            {
                int pointerCount = event.getPointerCount();
                System.out.println(pointerCount);
                break;
            }
        }
        /*
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                // If this is the first thing down on the screen
                //newPointer(event.getPointerId(0), event.getX(), event.getY());
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                // If there is already something pointing to the screen and
                // a new pointer is added to the screen
                int pId = event.getActionIndex();
                //newPointer(event.getPointerId(pId), event.getX(pId), event.getY(pId));
                break;
            }


            case MotionEvent.ACTION_UP: {
                // The last (only) pointer on the screen is lifted
                int pId = event.getPointerId(0);
                //mMap.remove(pId);
                //invalidate();  // Makes the View draw again, erasing the pointer lifted
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                // One of many pointers on the screen is lifted
                int pId = event.getPointerId(event.getActionIndex());
                //mMap.remove(pId);
                //invalidate();  // Makes the View draw again, erasing the pointer lifted
                break;
            }
        }
        */
        return true;
    }
}
