package com.example.appbanthietbidientu.model.EventBus;

import com.example.appbanthietbidientu.model.Sanphammoi;

public class SuaXoaEvent {
  Sanphammoi sanphammoi;
    public SuaXoaEvent(Sanphammoi sanphammoi) {

        this.sanphammoi = sanphammoi;
    }
    public Sanphammoi getSanphammoi() {

        return sanphammoi;
    }

    public void setSanphammoi(Sanphammoi sanphammoi) {

        this.sanphammoi = sanphammoi;
    }
}
