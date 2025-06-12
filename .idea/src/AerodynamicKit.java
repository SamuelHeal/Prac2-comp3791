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
    
    // Get effective drag coefficient with upgrade modifications
    public double getEffectiveDragCoefficient() {
        return aeroPackage.dragCoefficient * (1 - (upgradeLevel - 1) * 0.03);
    }
    
    // Get effective downforce with upgrade modifications
    public int getEffectiveDownforce() {
        return (int)(aeroPackage.downforce * (1 + (upgradeLevel - 1) * 0.08));
    }
    
    // Get effective top speed with upgrade modifications
    public int getEffectiveTopSpeed() {
        return (int)(aeroPackage.topSpeed * (1 + (upgradeLevel - 1) * 0.02));
    }
    
    // Get effective fuel efficiency with upgrade modifications
    public double getEffectiveFuelEfficiency() {
        return aeroPackage.fuelEfficiency * (1 + (upgradeLevel - 1) * 0.05);
    }
    
    // Get effective cornering ability with upgrade modifications
    public int getEffectiveCorneringAbility() {
        return Math.min(10, aeroPackage.corneringAbility + (upgradeLevel - 1));
    }
    
    public AeroPackage getPackage() { return aeroPackage; }
    public int getUpgradeLevel() { return upgradeLevel; }
}