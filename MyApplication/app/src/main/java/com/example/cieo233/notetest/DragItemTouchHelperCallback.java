package com.example.cieo233.notetest;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by Cieo233 on 1/28/2017.
 */

public class DragItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private OnItemTouchCallbackListener onItemTouchCallbackListener;



    public DragItemTouchHelperCallback(OnItemTouchCallbackListener onItemTouchCallbackListener) {
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;

            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (onItemTouchCallbackListener != null){
            onItemTouchCallbackListener.onSelectedChanged(viewHolder, actionState);
        }
        Log.e("TestOnSelectedChanged", String.valueOf(actionState));
    }

    @Override
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {

        RecyclerView.ViewHolder winner = super.chooseDropTarget(selected, dropTargets, curX, curY);
        if (onItemTouchCallbackListener != null){
            onItemTouchCallbackListener.chooseDropTarget(selected,winner);
        }
        return winner;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }



    public interface OnItemTouchCallbackListener {
        void chooseDropTarget(RecyclerView.ViewHolder selected,RecyclerView.ViewHolder winner);
        void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);
    }


}
