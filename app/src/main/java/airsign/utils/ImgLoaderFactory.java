package airsign.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import loop.airsign.loop.R;


public class ImgLoaderFactory
{
    public static ImageLoader getInstance(Context ctx)
    {
        ImageLoader res = ImageLoader.getInstance();
        if (res.isInited()) return res;
        else
        {
            DisplayImageOptions dio = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .showImageOnLoading(R.drawable.default_img)
                    .showImageOnFail(R.drawable.default_img)
                    .displayer(new FadeInBitmapDisplayer(500, true, true, false))
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx)
                    .defaultDisplayImageOptions(dio)
                    .threadPoolSize(3)
                    .memoryCache(new WeakMemoryCache())
                    .build();
            res.init(config);
            return res;
        }
    }

}