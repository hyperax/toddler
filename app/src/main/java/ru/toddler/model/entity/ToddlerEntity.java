package ru.toddler.model.entity;

import android.support.annotation.NonNull;

import nl.qbusict.cupboard.annotation.Column;
import ru.toddler.model.entity.contract.Contract;
import ru.toddler.util.NpeUtils;

public class ToddlerEntity {

    @Column(Contract.ID)
    public long id;

    @NonNull
    @Column(Contract.ToddlerEntity.NAME)
    public String name = NpeUtils.EMPTY_STRING;

    @Column(Contract.ToddlerEntity.AGE)
    public int age = 1;
}
