package com.magicleap.cameracameleon;

import android.graphics.Bitmap;

public interface WebServiceEvents {

    public void onSuccess(Bitmap bitmap);

    public void onFailure(String message);

}
