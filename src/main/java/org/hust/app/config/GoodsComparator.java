package org.hust.app.config;

import org.hust.app.entity.Goods;

import java.util.Comparator;

public class GoodsComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        String uid1 = ((Goods) o1).getUid();
        String uid2 = ((Goods) o2).getUid();
        return uid1.compareTo(uid2);
    }
}
