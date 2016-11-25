package ru.toddler.model.entity.contract;

import android.provider.BaseColumns;

public interface Contract {

    String ID = BaseColumns._ID;

    Class[] ENTITIES = {
            ru.toddler.model.entity.ToddlerEntity.class
    };

    interface ToddlerEntity {
        String NAME = "name";
        String AGE = "age";
    }
}
