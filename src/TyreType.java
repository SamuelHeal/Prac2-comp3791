public enum TyreType {
    SOFT(1.2, 0.7, 25, 1.2),
    MEDIUM(1.0, 1.0, 40, 1.0),
    HARD(0.9, 1.3, 60, 0.9);
    
    public final double gripMultiplier;
    public final double durabilityMultiplier;
    public final int maxLaps;
    public final double corneringMultiplier;
    
    TyreType(double grip, double durability, int laps, double cornering) {
        this.gripMultiplier = grip;
        this.durabilityMultiplier = durability;
        this.maxLaps = laps;
        this.corneringMultiplier = cornering;
    }
    
    public double getCorneringMultiplier() { return corneringMultiplier; }
}