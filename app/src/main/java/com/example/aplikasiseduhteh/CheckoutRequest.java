package com.example.aplikasiseduhteh;

import java.util.List;
public class CheckoutRequest {
    public List<Item> items;

    public CheckoutRequest(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        public int produk_id;
        public int jumlah_beli;

        public Item(int produkId, int jumlahBeli) {
            this.produk_id = produkId;
            this.jumlah_beli = jumlahBeli;
        }
    }
}
