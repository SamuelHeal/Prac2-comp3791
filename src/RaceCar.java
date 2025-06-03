// Race Car Class
class RaceCar {
    private String name;
    private Engine engine;
    private Tyres tyres;
    private AerodynamicKit aeroKit;
    private double currentFuel;
    private double maxFuelCapacity;
    
    public RaceCar(String name) {
        this.name = name;
        this.engine = new Engine(EngineType.STANDARD);
        this.tyres = new Tyres(TyreType.MEDIUM);
        this.aeroKit = new AerodynamicKit(AeroPackage.STANDARD_AERODYNAMICS);
        this.maxFuelCapacity = 100.0;
        this.currentFuel = maxFuelCapacity;
    }
    
    public void customizeEngine(EngineType type) {
        this.engine = new Engine(type);
    }
    
    public void customizeTyres(TyreType type) {
        this.tyres = new Tyres(type);
    }
    
    public void customizeAero(AeroPackage aero) {
        this.aeroKit = new AerodynamicKit(aero);
    }
    
    public void refuel(double amount) {
        currentFuel = Math.min(maxFuelCapacity, currentFuel + amount);
    }
    
    public void consumeFuel(double amount) {
        currentFuel = Math.max(0, currentFuel - amount);
    }
    
    public double getOverallSpeed(Track track, WeatherCondition weather) {
        // Base speed calculation using aerodynamic top speed and engine multipliers
        double baseSpeed = aeroKit.getPackage().topSpeed;
        double engineSpeed = baseSpeed * engine.getSpeedMultiplier();
        double tyreSpeed = engineSpeed * tyres.getGripMultiplier();
        
        // Adjust for track type - downforce helps on technical tracks
        if (track.getType().equals("Street")) {
            double downforceBonus = Math.min(0.2, aeroKit.getPackage().downforce / 2000.0);
            tyreSpeed *= (1.0 + downforceBonus);
        }
        
        return tyreSpeed * weather.speedReduction;
    }
    
    public double getFuelConsumptionPerLap(Track track, WeatherCondition weather) {
        // Base fuel consumption calculation using aerodynamic efficiency
        double baseFuelPerKm = 1.0 / aeroKit.getPackage().fuelEfficiency;
        double lapConsumption = baseFuelPerKm * track.getLapLength();
        
        // Engine efficiency affects consumption
        double engineConsumption = lapConsumption / engine.getFuelEfficiency();
        
        // Drag coefficient affects fuel consumption
        double dragPenalty = 1.0 + (aeroKit.getPackage().dragCoefficient - 0.3) * 0.5;
        double aeroConsumption = engineConsumption * dragPenalty;
        
        // Track and weather effects
        double trackConsumption = aeroConsumption * track.getFuelMultiplier();
        
        return trackConsumption * weather.fuelConsumptionIncrease;
    }
    
    public double getTyreWearPerLap(Track track, WeatherCondition weather) {
        double baseWear = 2.0;
        double trackWear = baseWear * track.getTyreWearMultiplier();
        double tyreWear = trackWear / tyres.getType().durabilityMultiplier;
        
        // High downforce increases tyre wear due to increased grip forces
        double downforceWear = 1.0 + (aeroKit.getPackage().downforce / 1000.0) * 0.1;
        tyreWear *= downforceWear;
        
        return tyreWear * weather.tyreWearIncrease;
    }
    
    // Getters
    public String getName() { return name; }
    public Engine getEngine() { return engine; }
    public Tyres getTyres() { return tyres; }
    public AerodynamicKit getAeroKit() { return aeroKit; }
    public double getCurrentFuel() { return currentFuel; }
    public double getMaxFuelCapacity() { return maxFuelCapacity; }
}