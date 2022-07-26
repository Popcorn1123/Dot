/*
 * Decompiled with CFR 0_132.
 */
package xyz.Dot.event.events.rendering;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import xyz.Dot.event.Event;

public class EventRenderCape
extends Event {
    private ResourceLocation capeLocation;
    private final EntityPlayer player;

    public EventRenderCape(ResourceLocation capeLocation, EntityPlayer player) {
        this.capeLocation = capeLocation;
        this.player = player;
    }

    public ResourceLocation getLocation() {
        return this.capeLocation;
    }

    public void setLocation(ResourceLocation location) {
        this.capeLocation = location;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}

