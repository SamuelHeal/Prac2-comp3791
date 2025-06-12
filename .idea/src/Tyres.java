class Tyres {
    private TyreType type;
    private double currentWear;
    
    public Tyres(TyreType type) {
        this.type = type;
        this.currentWear = 0.0;
    }
    
    public void addWear(double wear) {
        currentWear += wear;
    }
    
    public boolean needsReplacement() {
        return currentWear >= 100.0;
    }
    
    public void replace() {
        currentWear = 0.0;
    }
    
    public double getGripMultiplier() {
        double wearPenalty = Math.max(0, 1 - (currentWear / 100.0) * 0.3);
        return type.gripMultiplier * wearPenalty;
    }
    
    public TyreType getType() { return type; }
    public double getCurrentWear() { return currentWear; }
}