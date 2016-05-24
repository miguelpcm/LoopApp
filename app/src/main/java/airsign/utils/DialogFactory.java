package airsign.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import loop.airsign.loop.R;

public class DialogFactory
{

    /*
        Example:
        DialogFactory.getYesNoDialog(ActivityName.this,
            R.string.no_network, R.string.activate_network, yes, no);
     */
    public static AlertDialog getYesNoDialog(Context ctx, int resTitle, int resMsg,
        DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no)
    {
        return new AlertDialog.Builder(ctx)
            .setTitle(resTitle)
            .setMessage(resMsg)
            .setPositiveButton(android.R.string.yes, yes)
            .setNegativeButton(android.R.string.no, no)
            .setIcon(R.mipmap.ic_launcher)
            .create();
    }
}
