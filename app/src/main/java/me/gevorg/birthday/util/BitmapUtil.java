package me.gevorg.birthday.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Utility class for Bitmap manipulation.
 *
 * @author Gevorg Harutyunyan.
 */
public class BitmapUtil {
    /**
     * Get contact photo bitmap.
     *
     * @param context context.
     * @param photo photo uri.
     * @return bitmap.
     */
    public static Bitmap getContactBitmapFromURI(Context context, String photo) {
        // Get photo uri.
        Uri uri = Uri.parse(photo);

        try {
            // Get input.
            InputStream input = context.getContentResolver().openInputStream(uri);

            if (input == null) {
                return null;
            }

            return getRoundedCornerBitmap(BitmapFactory.decodeStream(input));
        } catch (FileNotFoundException ignored) {}

        return null;
    }

    /**
     * Create rounded corner bitmap.
     *
     * @param bitmap bitmap.
     * @return rounded bitmap.
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap source = Bitmap.createScaledBitmap(bitmap, 480, 480, false);
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = source.getWidth();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(source, rect, rect, paint);

        return output;
    }

}
