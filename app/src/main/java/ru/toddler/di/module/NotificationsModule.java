package ru.toddler.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import ru.toddler.view.notification.Message;

@Module
public abstract class NotificationsModule {

    public NotificationsModule(@NonNull Context context) {
        Message.init(context);
    }
}
