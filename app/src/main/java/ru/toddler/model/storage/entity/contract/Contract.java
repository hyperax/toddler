package ru.toddler.model.storage.entity.contract;

import android.provider.BaseColumns;

public interface Contract {

    String ID = BaseColumns._ID;

    Class[] ENTITIES = {
            ru.toddler.model.storage.entity.ToddlerEntity.class
    };

    interface ToddlerEntity {
        String NAME = "name";
        String AGE = "age";
    }
}
