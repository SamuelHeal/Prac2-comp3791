// Pit Stop Strategy Classes
class PitStop {
    private int lap;
    private boolean changeTyres;
    private TyreType newTyreType;
    private double fuelAmount;
    private double timeDelay;
    private double acceleration;
    
    public PitStop(int lap, boolean changeTyres, TyreType newTyres, double fuel, double acceleration) {
        this.lap = lap;
        this.changeTyres = changeTyres;
        this.newTyreType = newTyres;
        this.fuelAmount = fuel;
        this.acceleration = acceleration;
        this.timeDelay = calculateDelay();
    }
    
    private double calculateDelay() {
        double baseTime = 20.0; // 20 seconds base
        if (changeTyres) baseTime += 15.0;
        baseTime += fuelAmount * 0.5; // 0.5 seconds per liter
        // Acceleration effect: for every 0.1s below 6.0s, reduce by 0.5% (max 10%)
        double accelBonus = Math.max(0, (6.0 - acceleration) * 0.5); // 0.5% per 0.1s
        accelBonus = Math.min(accelBonus, 10.0); // cap at 10%
        baseTime *= (1.0 - accelBonus / 100.0);
        return baseTime;
    }
    
    public int getLap() { return lap; }
    public boolean isChangeTyres() { return changeTyres; }
    public TyreType getNewTyreType() { return newTyreType; }
    public double getFuelAmount() { return fuelAmount; }
    public double getTimeDelay() { return timeDelay; }
}