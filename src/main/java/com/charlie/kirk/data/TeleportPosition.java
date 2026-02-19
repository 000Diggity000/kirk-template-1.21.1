package com.charlie.kirk.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.FireworkExplosion;

import java.util.ArrayList;
import java.util.List;

public record TeleportPosition(String name, int x, int y, int z) {
    public static final TeleportPosition DEFAULT;
    public static final Codec<TeleportPosition> CODEC;
    public static final StreamCodec<ByteBuf, TeleportPosition> TELEPORT_POSITION_STREAM_CODEC;
    static {
        DEFAULT = new TeleportPosition("",0,0,0);
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(TeleportPosition::name),
                        Codec.INT.fieldOf("x").forGetter(TeleportPosition::x),
                        Codec.INT.fieldOf("y").forGetter(TeleportPosition::y),
                        Codec.INT.fieldOf("z").forGetter(TeleportPosition::z)
                ).apply(instance,TeleportPosition::new)
        );
        TELEPORT_POSITION_STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, TeleportPosition::name,
                ByteBufCodecs.INT, TeleportPosition::x,
                ByteBufCodecs.INT, TeleportPosition::y,
                ByteBufCodecs.INT, TeleportPosition::z,
                TeleportPosition::new
        );

    }
}
