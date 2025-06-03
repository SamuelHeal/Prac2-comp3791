enum EngineType {
    STANDARD(1.0, 1.0, 1.0),
    TURBOCHARGED(1.3, 0.8, 1.4),
    HYBRID(1.1, 1.2, 0.9);
    
    public final double speedMultiplier;
    public final double fuelEfficiency;
    public final double reliabilityMultiplier;
    
    EngineType(double speed, double fuel, double reliability) {
        this.speedMultiplier = speed;
        this.fuelEfficiency = fuel;
        this.reliabilityMultiplier = reliability;
    }
}