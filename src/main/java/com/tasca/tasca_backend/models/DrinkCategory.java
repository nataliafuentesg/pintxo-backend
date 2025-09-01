package com.tasca.tasca_backend.models;

import java.util.Optional;

public enum DrinkCategory {
    SIGNATURE_COCKTAILS("Signature Cocktails", "signature-cocktails"),

    WINES_BY_THE_GLASS("Wines by the Glass", "wines-by-the-glass"),
    WINE_BOTTLES("Wine Bottles", "wine-bottles"),

    BEER_LIST("Beer List", "beer-list"),
    BEER_GLUTEN_FREE("Beer (Gluten Free)", "beer-gluten-free"),
    BEER_NON_ALCOHOLIC("Beer (Non-Alcoholic)", "beer-non-alcoholic"),
    CIDER("Cider", "cider"),

    SANGRIAS("Sangrias", "sangrias"),

    CORDIALS("Cordials", "cordials"),
    SHERRY_AND_JEREZ("Sherry & Jerez", "sherry-and-jerez"),
    PORTS("Ports", "ports"),

    COFFEES("Coffees", "coffees"),

    SOFT_DRINKS("Soft Drinks", "soft-drinks"),   // gaseosas
    JUICES("Juices", "juices");


    private final String displayName;
    private final String slug;

    DrinkCategory(String displayName, String slug) {
        this.displayName = displayName;
        this.slug = slug;
    }
    public String getDisplayName() { return displayName; }
    public String getSlug() { return slug; }

    public static Optional<DrinkCategory> fromAny(String value) {
        if (value == null) return Optional.empty();
        String v = value.trim();
        for (DrinkCategory c : values()) {
            if (c.name().equalsIgnoreCase(v) || c.getSlug().equalsIgnoreCase(v) || c.getDisplayName().equalsIgnoreCase(v)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}