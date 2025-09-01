package com.tasca.tasca_backend.models;

public enum MenuCategory {
    GREEN_TAPAS("Green Tapas", "green-tapas"),
    MEAT_TAPAS("Meat Tapas", "meat-tapas"),
    SEAFOOD_TAPAS("Seafood Tapas", "seafood-tapas"),
    PINTXOS("Pintxos", "pintxos"),
    PAELLAS("Paellas", "paellas"),
    CHEESE_BOARDS("Cheese Boards", "cheese-boards");

    private final String displayName;
    private final String slug;

    MenuCategory(String displayName, String slug) {
        this.displayName = displayName;
        this.slug = slug;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSlug() {
        return slug;
    }

}
