package com.vgb;

import com.vgb.*;
import java.util.UUID;

public class ItemFactory {
    public static Item create(String type, UUID id, String desc, int qty, double price) {
        switch (type.toLowerCase()) {
            case "equipment": return new Equipment(id, desc, qty, price);
            case "lease":     return new Lease(id, desc, qty, price);
            case "rental":    return new Rental(id, desc, qty, price);
            case "material":  return new Material(id, desc, qty, price);
            case "contract":  return new Contract(id, desc, qty, price);
            default:          return new Item(id, desc, qty, price);
        }
    }
}