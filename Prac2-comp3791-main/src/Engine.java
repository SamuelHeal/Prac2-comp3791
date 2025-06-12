// Car Component Classes
class Engine {
    private EngineType type;
    private int upgradeLevel;
    
    public Engine(EngineType type) {
        this.type = type;
        this.upgradeLevel = 1;
    }
    
    public void upgrade() {
        if (upgradeLevel < 5) {
            upgradeLevel++;
        }
    }
    
    public double getSpeedMultiplier() {
        return type.speedMultiplier * (1 + (upgradeLevel - 1) * 0.1);
    }
    
    public double getFuelEfficiency() {
        return type.fuelEfficiency * (1 + (upgradeLevel - 1) * 0.05);
    }
    
    public double getAcceleration() {
        return type.acceleration;
    }
    
    public double getEffectiveAcceleration() {
        // Each upgrade reduces acceleration time by 3% per level above 1
        return type.acceleration * (1 - 0.03 * (upgradeLevel - 1));
    }
    
    public EngineType getType() { return type; }
    public int getUpgradeLevel() { return upgradeLevel; }
}
