package com.vanilla3dplus.visual;

/** Mutable per-client state shared by visual effect systems. */
public final class VisualState {
    public int tick;
    public double previousY;
    public double previousVerticalVelocity;
    public boolean wasInWater;
    public boolean wasOnGround;
    public boolean wasRaining;
    public double lastFallDistance;
}
