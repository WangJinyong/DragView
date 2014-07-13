/*
 * This is a modified version of a class from the Android Open Source Project. 
 * The original copyright and license information follows.
 * 
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blahti.example.drag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A ViewGroup that coordinates dragging across its dscendants.
 *
 * <p> This class used DragLayer in the Android Launcher activity as a model.
 * It is a bit different in several respects:
 * (1) It extends MyAbsoluteLayout rather than FrameLayout; (2) it implements DragSource and DropTarget methods
 * that were done in a separate Workspace class in the Launcher.
 */
public class DragLayer extends MyAbsoluteLayout 
    implements DragSource, DropTarget, View.OnTouchListener
{
    DragController mDragController;

    /**
     * Used to create a new DragLayer from XML.
     *
     * @param context The application's context.
     * @param attrs The attribtues set containing the Workspace's customization values.
     */
    public DragLayer (Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public void setDragController(DragController controller) {
        mDragController = controller;
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragController.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDragController.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        return mDragController.dispatchUnhandledMove(focused, direction);
    }

/**
 */
// DragSource interface methods

/**
  * This method is called to determine if the DragSource has something to drag.
  * 
  * @return True if there is something to drag
  */

public boolean allowDrag () {
    // In this simple demo, any view that you touch can be dragged.
    return true;
}

/**
 * setDragController
 *
 */

 /* setDragController is already defined. See above. */

/**
 * onDropCompleted
 *
 */

public void onDropCompleted (View target, boolean success)
{
    toast ("DragLayer2.onDropCompleted: " + target.getId () + " Check that the view moved.");
}

/**
 */
// DropTarget interface implementation

/**
 * Handle an object being dropped on the DropTarget.
 * This is the where a dragged view gets repositioned at the end of a drag.
 * 
 * @param source DragSource where the drag started
 * @param x X coordinate of the drop location
 * @param y Y coordinate of the drop location
 * @param xOffset Horizontal offset with the object being dragged where the original
 *          touch happened
 * @param yOffset Vertical offset with the object being dragged where the original
 *          touch happened
 * @param dragView The DragView that's being dragged around on screen.
 * @param dragInfo Data associated with the object being dragged
 * 
 */
public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset,
        DragView dragView, Object dragInfo)
{
    View v = (View) dragInfo;
    toast ("DragLayer2.onDrop accepts view: " + v.getId ()
          + "x, y, xO, yO :" + new Integer (x) + ", " + new Integer (y) + ", "
          + new Integer (xOffset) + ", " + new Integer (yOffset));

    int w = v.getWidth ();
    int h = v.getHeight ();
    int left = x - xOffset;
    int top = y - yOffset;
    DragLayer.LayoutParams lp = new DragLayer.LayoutParams (w, h, left, top);
    this.updateViewLayout(v, lp);
}

public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset,
        DragView dragView, Object dragInfo)
{
}

public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset,
        DragView dragView, Object dragInfo)
{
}

public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset,
        DragView dragView, Object dragInfo)
{
}

/**
 * Check if a drop action can occur at, or near, the requested location.
 * This may be called repeatedly during a drag, so any calls should return
 * quickly.
 * 
 * @param source DragSource where the drag started
 * @param x X coordinate of the drop location
 * @param y Y coordinate of the drop location
 * @param xOffset Horizontal offset with the object being dragged where the
 *            original touch happened
 * @param yOffset Vertical offset with the object being dragged where the
 *            original touch happened
 * @param dragView The DragView that's being dragged around on screen.
 * @param dragInfo Data associated with the object being dragged
 * @return True if the drop will be accepted, false otherwise.
 */
public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset,
        DragView dragView, Object dragInfo)
{
    return true;
}

/**
 * Estimate the surface area where this object would land if dropped at the
 * given location.
 * 
 * @param source DragSource where the drag started
 * @param x X coordinate of the drop location
 * @param y Y coordinate of the drop location
 * @param xOffset Horizontal offset with the object being dragged where the
 *            original touch happened
 * @param yOffset Vertical offset with the object being dragged where the
 *            original touch happened
 * @param dragView The DragView that's being dragged around on screen.
 * @param dragInfo Data associated with the object being dragged
 * @param recycle {@link Rect} object to be possibly recycled.
 * @return Estimated area that would be occupied if object was dropped at
 *         the given location. Should return null if no estimate is found,
 *         or if this target doesn't provide estimations.
 */
public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo, Rect recycle)
{
    return null;
}

/**
 */
// More methods

/**
 * Show a string on the screen via Toast.
 * 
 * @param msg String
 * @return void
 */

public void toast (String msg)
{
    if (!DragActivity.Debugging) return;
    Toast.makeText (getContext (), msg, Toast.LENGTH_SHORT).show ();
} // end toast

protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
protected int colCount, childSize, padding, dpi, scroll = 0;
protected float lastDelta = 0;
protected boolean enabled = true, touching = false;
protected Handler handler = new Handler();
protected Runnable updateTask = new Runnable() {
	@SuppressLint("WrongCall")
	public void run() {
		if (dragged != -1) {
			if (lastY < padding * 3 && scroll > 0)
				scroll -= 20;
			else if (lastY > getBottom() - getTop() - (padding * 3)
					&& scroll < getMaxScroll())
				scroll += 20;
		} else if (lastDelta != 0 && !touching) {
			scroll += lastDelta;
			lastDelta *= .9;
			if (Math.abs(lastDelta) < .25)
				lastDelta = 0;
		}
		clampScroll();
		onLayout(true, getLeft(), getTop(), getRight(), getBottom());
		
		handler.postDelayed(this, 25);
	}
};

protected void clampScroll() {
	int stretch = 3, overreach = getHeight() / 2;
	int max = getMaxScroll();
	max = Math.max(max, 0);

	if (scroll < -overreach) {
		scroll = -overreach;
		lastDelta = 0;
	} else if (scroll > max + overreach) {
		scroll = max + overreach;
		lastDelta = 0;
	} else if (scroll < 0) {
		if (scroll >= -stretch)
			scroll = 0;
		else if (!touching)
			scroll -= scroll / stretch;
	} else if (scroll > max) {
		if (scroll <= max + stretch)
			scroll = max;
		else if (!touching)
			scroll += (max - scroll) / stretch;
	}
}

protected int getMaxScroll() {
	int rowCount = (int) Math.ceil((double) getChildCount() / colCount), max = rowCount
			* childSize + (rowCount + 1) * padding - getHeight();
	return max;
}

protected Point getCoorFromIndex(int index) {
	int col = index % colCount;
	int row = index / colCount;
	return new Point(padding + (childSize + padding) * col, padding
			+ (childSize + padding) * row - scroll);
}


@SuppressLint("WrongCall")
@Override
public boolean onTouch(View arg0, MotionEvent event) {
	// TODO Auto-generated method stub
	int action = event.getAction();
	switch (action & MotionEvent.ACTION_MASK) {
	case MotionEvent.ACTION_DOWN:
		enabled = true;
		lastX = (int) event.getX();
		lastY = (int) event.getY();
		touching = true;
		break;
	case MotionEvent.ACTION_MOVE:
		int delta = lastY - (int) event.getY();
		/*if (dragged != -1) {
			// change draw location of dragged visual
			int x = (int) event.getX(), y = (int) event.getY();
			int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
			getChildAt(dragged).layout(l, t, l + (childSize * 3 / 2),
					t + (childSize * 3 / 2));

			// check for new target hover
			int target = getTargetFromCoor(x, y);
			if (lastTarget != target) {
				if (target != -1) {
					animateGap(target);
					lastTarget = target;
				}
			}
		} else {*/
			scroll += delta;
			clampScroll();
			if (Math.abs(delta) > 2)
				enabled = false;
			onLayout(true, getLeft(), getTop(), getRight(), getBottom());
		//}
		lastX = (int) event.getX();
		lastY = (int) event.getY();
		lastDelta = delta;
		break;
	case MotionEvent.ACTION_UP:
		/*if (dragged != -1) {
			View v = getChildAt(dragged);
			if (lastTarget != -1)
				reorderChildren();
			else {
				Point xy = getCoorFromIndex(dragged);
				v.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize);
			}
			v.clearAnimation();
			if (v instanceof ImageView)
				((ImageView) v).setAlpha(255);
			lastTarget = -1;
			dragged = -1;
		}*/
		touching = false;
		break;
	}
	if (dragged != -1)
		return true;
	return false;
}

public static float childRatio = .9f;
@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
	// TODO Auto-generated method stub
	super.onLayout(changed, l, t, r, b);
	Log.d("DragLayer", "scroll:"+scroll);
	// compute width of view, in dp
			//float w = (r - l) / (dpi / 160f);

			// determine number of columns, at least 2
			colCount = 1;
			/*int sub = 240;
			w -= 280;
			while (w > 0) {
				colCount++;
				w -= sub;
				sub += 40;
			}*/

			// determine childSize and padding, in px
			childSize = (r - l) / colCount;
			childSize = Math.round(childSize * childRatio);
			padding = ((r - l) - (childSize * colCount)) / (colCount + 1);

			for (int i = 0; i < getChildCount(); i++)
				if (i != dragged) {
					Point xy = getCoorFromIndex(i);
					getChildAt(i).layout(xy.x, xy.y, xy.x + childSize,
							xy.y + childSize);
				}
}


} // end class
