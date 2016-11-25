package ru.toddler.view.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import ru.toddler.util.NpeUtils;

public class SpinnerProgressDialog extends BaseDialogFragment {

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_MESSAGE = "arg_message";
    private static final String ARG_IS_CANCELABLE = "arg_is_cancelable";

    public static SpinnerProgressDialog newInstance(String message, boolean isCancelable) {
        return newInstance(null, message, isCancelable);
    }

    public static SpinnerProgressDialog newInstance(String title, String message, boolean isCancelable) {
        SpinnerProgressDialog dialog = new SpinnerProgressDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putBoolean(ARG_IS_CANCELABLE, isCancelable);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(getArguments().getBoolean(ARG_IS_CANCELABLE, false));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        String title = getArguments().getString(ARG_TITLE, null);
        if (!NpeUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setMessage(getArguments().getString(ARG_MESSAGE));
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }
}
