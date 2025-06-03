class AerodynamicKit {
    private AeroPackage aeroPackage;
    private int upgradeLevel;
    
    public AerodynamicKit(AeroPackage aeroPackage) {
        this.aeroPackage = aeroPackage;
        this.upgradeLevel = 1;
    }
    
    public void upgrade() {
        if (upgradeLevel < 3) {
            upgradeLevel++;
        }
    }
    
    public double getStraightLineSpeedMultiplier() {
        return aeroPackage.straightLineSpeed * (1 + (upgradeLevel - 1) * 0.05);
    }
    
    public double getCorneringSpeedMultiplier() {
        return aeroPackage.corneringSpeed * (1 + (upgradeLevel - 1) * 0.08);
    }
    
    public AeroPackage getPackage() { return aeroPackage; }
    public int getUpgradeLevel() { return upgradeLevel; }
}