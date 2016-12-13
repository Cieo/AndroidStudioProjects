package com.example.cieo233.unittest;

import android.view.View;

/**
 * Created by Cieo233 on 12/11/2016.
 */

public class Interface {
    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(Object data);

        public void recyclerViewListLongClicked(Object data);
    }

    public interface AwesomeTextChannelClickListener{
        public void awesomeTextChannelClicked(Object data);
    }

}
