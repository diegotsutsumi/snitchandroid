package com.alieeen.snitch.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by alinekborges on 16/04/15.
 */
public class ImageHandler {

    public static void Save(Context context, Bitmap b, String imgName) {
        try {
            if (Load(context, imgName) != null)    // Prote��o para n�o salvar imagem com o mesmo nome
                return;

            File file = new File(context.getApplicationContext().getFilesDir().getAbsolutePath(), imgName);
            //FileOutputStream fos = new FileOutputStream(context.getApplicationContext().getFilesDir().getAbsolutePath() + imgName);
            FileOutputStream fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap Load(Context context, String imgName) {
        try {
            File file = new File(context.getApplicationContext().getFilesDir().getAbsolutePath(), imgName);
            //FileInputStream fis = context.openFileInput(file);
            FileInputStream fis = new FileInputStream(file);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void Delete(Context context, String imgName) {
        File file = new File(context.getApplicationContext().getFilesDir().getAbsolutePath(), imgName);
        file.delete();
    }
}
