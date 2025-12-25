package com.charlie.kirk.datagen;

import com.charlie.kirk.Kirk;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class KirkLanguageProvider extends LanguageProvider {
    public KirkLanguageProvider(PackOutput output) {
        super(
                // Provided by the GatherDataEvent.
                output,
                // Your mod id.
                "kirk",
                // The locale to use. You may use multiple language providers for different locales.
                "en_us"
        );
    }

    @Override
    protected void addTranslations() {
        // Adds a translation with the given key and the given value.
        //add("translation.key.1", "Translation 1");
        add(Kirk.BAT_ITEM.get(), "Bat");
        add(Kirk.COTTON.get(), "Cotton");
        add(Kirk.SAHUR.get(), "Tung Tung Tung Sahur");
        add(Kirk.SILO_BLOCK_ITEM.get(), "Silo");
    }
}
