package com.lgallardo.qbittorrentclient;

import android.app.Dialog;
import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextPreferenceAutoClose extends EditTextPreference {
    public EditTextPreferenceAutoClose(Context context)
    {
        super(context);
    }

    public EditTextPreferenceAutoClose(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EditTextPreferenceAutoClose(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAddEditTextToDialogView(View dialogView, EditText editText)
    {
        super.onAddEditTextToDialogView(dialogView, editText);

        // editText.setImeOptions(EditorInfo.IME_ACTION_DONE); - uncomment if it's not specified in preferences.xml

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    onClick(getDialog(), Dialog.BUTTON_POSITIVE);
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
    }
}
