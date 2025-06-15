public enum WeatherCondition {
    DRY(1.0, 1.0, 1.0),
    WET(0.8, 1.3, 1.2),
    EXTREME_WET(0.6, 1.6, 1.5);
    
    public final double speedReduction;
    public final double tyreWearIncrease;
    public final double fuelConsumptionIncrease;
    
    WeatherCondition(double speed, double wear, double fuel) {
        this.speedReduction = speed;
        this.tyreWearIncrease = wear;
        this.fuelConsumptionIncrease = fuel;
    }
}
