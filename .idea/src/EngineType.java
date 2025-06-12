enum EngineType {
    STANDARD(1.0, 1.0, 1.0, 6.0),
    TURBOCHARGED(1.3, 0.8, 1.4, 4.2),
    HYBRID(1.1, 1.2, 0.9, 5.0);
    
    public final double speedMultiplier;
    public final double fuelEfficiency;
    public final double reliabilityMultiplier;
    public final double acceleration; // 0-100 km/h time in seconds
    
    EngineType(double speed, double fuel, double reliability, double acceleration) {
        this.speedMultiplier = speed;
        this.fuelEfficiency = fuel;
        this.reliabilityMultiplier = reliability;
        this.acceleration = acceleration;
    }
}