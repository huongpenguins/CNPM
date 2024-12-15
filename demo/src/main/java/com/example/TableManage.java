package com.example;

import javafx.scene.control.TableView;

public class TableManage<S> {
    TableView<S> table;

    public TableManage (TableView<S> table){
        this.table = table;
    }

}
