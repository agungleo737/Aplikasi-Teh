package com.example.aplikasiseduhteh;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    public static List<Teh> keranjangList = new ArrayList<>();
    public static boolean tambahproduk(Teh tehDipilih, int qtyBaru, int stokTersedia) {
        for (Teh item : keranjangList) {
            if (item.getNama().equalsIgnoreCase(tehDipilih.getNama())) {
                int totalBaru = item.getQtyDibeli() + qtyBaru;
                if (totalBaru > stokTersedia) {
                    return false;
                }
                item.setQtyDibeli(totalBaru);
                return true;
            }
        }
        // Item belum ada di keranjang
        if (qtyBaru > stokTersedia) return false;
        tehDipilih.setQtyDibeli(qtyBaru);
        keranjangList.add(tehDipilih);
        return true;
    }
}