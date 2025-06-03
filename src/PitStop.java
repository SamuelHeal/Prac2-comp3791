// Pit Stop Strategy Classes
class PitStop {
    private int lap;
    private boolean changeTyres;
    private TyreType newTyreType;
    private double fuelAmount;
    private double timeDelay;
    
    public PitStop(int lap, boolean changeTyres, TyreType newTyres, double fuel) {
        this.lap = lap;
        this.changeTyres = changeTyres;
        this.newTyreType = newTyres;
        this.fuelAmount = fuel;
        this.timeDelay = calculateDelay();
    }
    
    private double calculateDelay() {
        double baseTime = 20.0; // 20 seconds base
        if (changeTyres) baseTime += 15.0;
        baseTime += fuelAmount * 0.5; // 0.5 seconds per liter
        return baseTime;
    }
    
    public int getLap() { return lap; }
    public boolean isChangeTyres() { return changeTyres; }
    public TyreType getNewTyreType() { return newTyreType; }
    public double getFuelAmount() { return fuelAmount; }
    public double getTimeDelay() { return timeDelay; }
}