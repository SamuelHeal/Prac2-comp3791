enum AeroPackage {
    LOW_DOWNFORCE(1.1, 0.9, 1.2),
    BALANCED(1.0, 1.0, 1.0),
    HIGH_DOWNFORCE(0.9, 1.2, 0.8);
    
    public final double straightLineSpeed;
    public final double corneringSpeed;
    public final double fuelConsumption;
    
    AeroPackage(double straight, double cornering, double fuel) {
        this.straightLineSpeed = straight;
        this.corneringSpeed = cornering;
        this.fuelConsumption = fuel;
    }
}