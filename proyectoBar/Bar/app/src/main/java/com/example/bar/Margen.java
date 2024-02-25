package com.example.bar;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Clase que sirve para poner margen a cada elemento del recyclerview
 */
public class Margen extends RecyclerView.ItemDecoration {
    private int espacio;
    private boolean horizontal;

    public Margen(int espacio, boolean horizontal) {
        this.espacio = espacio;
        this.horizontal = horizontal;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (this.horizontal){
            outRect.left = espacio;
        }else{
            outRect.left = espacio;
            outRect.top = espacio * 3;
        }

    }
}
