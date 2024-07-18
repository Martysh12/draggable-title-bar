package com.martysh12.legacystuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivitySheet extends LinearLayout {
    private final TextView title;
    private final FrameLayout titleBar;
    private final FrameLayout content;
    private final View mover;

    private DragState dragState = DragState.IDLE_EXPANDED;
    private int dragOffset = 0;

    private DragStateListener dragStateListener;

    public ActivitySheet(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.activity_sheet, this);

        title = findViewById(R.id.activity_sheet_title);
        titleBar = findViewById(R.id.activity_sheet_title_bar);
        content = findViewById(R.id.activity_sheet_content);
        mover = findViewById(R.id.activity_sheet_mover);

        setUpAttrs(attrs);
        setUpViews();

        // Enumerate children
        post(() -> {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);

                if (child.getId() == R.id.activity_sheet_root)
                    continue;

                removeView(child);
                content.addView(child);
            }
        });
    }

    private void setUpAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ActivitySheet);

        title.setText(a.getString(R.styleable.ActivitySheet_android_title));

        a.recycle();
    }

    private int getRelativeY(Integer rawY) {
        Rect r = new Rect();
        getGlobalVisibleRect(r);
        return rawY == null ? 0 : rawY - r.top;
    }

    private void moveTitleBar(int y) {
        int clampedY = Math.min(Math.max(y, 0), getHeight() - titleBar.getHeight());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mover.getLayoutParams();
        params.height = clampedY;
        mover.setLayoutParams(params);
    }

    private void setDragState(DragState dragState) {
        this.dragState = dragState;

        if (dragStateListener != null)
            dragStateListener.onDragStateChanged(dragState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpViews() {
        titleBar.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setDragState(DragState.DRAGGING);
                    dragOffset = (int) event.getY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (dragState == DragState.DRAGGING) {
                        moveTitleBar(getRelativeY((int) event.getRawY()) - dragOffset);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if ((getRelativeY((int) event.getRawY()) - dragOffset) + titleBar.getHeight() / 2 < getHeight() / 2) {
                        setDragState(DragState.IDLE_EXPANDED);
                        moveTitleBar(0);
                    } else {
                        setDragState(DragState.IDLE_COLLAPSED);
                        moveTitleBar(getHeight() - titleBar.getHeight());
                    }
                    break;
            }

            return true;
        });
    }

    public void setDragStateListener(DragStateListener dragStateListener) {
        this.dragStateListener = dragStateListener;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public enum DragState {
        IDLE_EXPANDED,
        IDLE_COLLAPSED,
        DRAGGING,
    }

    public interface DragStateListener {
        void onDragStateChanged(DragState dragState);
    }
}
