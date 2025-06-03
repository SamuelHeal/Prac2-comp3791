enum TyreType {
    SOFT(1.2, 0.7, 25),
    MEDIUM(1.0, 1.0, 40),
    HARD(0.9, 1.3, 60);
    
    public final double gripMultiplier;
    public final double durabilityMultiplier;
    public final int maxLaps;
    
    TyreType(double grip, double durability, int laps) {
        this.gripMultiplier = grip;
        this.durabilityMultiplier = durability;
        this.maxLaps = laps;
    }
}