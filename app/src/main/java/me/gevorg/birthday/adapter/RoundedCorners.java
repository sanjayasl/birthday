package me.gevorg.birthday.adapter;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

import me.gevorg.birthday.util.BitmapUtil;

/**
 * Rounded corner transformation for Picasso.
 *
 * @author Gevorg Harutyunyan.
 */
public class RoundedCorners implements Transformation {
    /**
     * Transform loaded bitmap.
     *
     * @param source source.
     * @return transformed bitmap.
     */
    @Override
    public Bitmap transform(Bitmap source) {
        // Generate new bitmap.
        Bitmap output = BitmapUtil.getRoundedCornerBitmap(source);

        // We don't need it anymore.
        source.recycle();

        // Return result.
        return output;
    }

    /**
     * Transformation key.
     *
     * @return return key.
     */
    @Override
    public String key() {
        return "rounded";
    }
}
