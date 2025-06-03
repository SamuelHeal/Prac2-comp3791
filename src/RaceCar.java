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
        this.aeroKit = new AerodynamicKit(AeroPackage.STANDARD_KIT);
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
        double baseSpeed = 100.0;
        double engineSpeed = baseSpeed * engine.getSpeedMultiplier();
        double tyreSpeed = engineSpeed * tyres.getGripMultiplier();
        
        if (track.getType().equals("Street")) {
            tyreSpeed *= aeroKit.getCorneringSpeedMultiplier();
        } else {
            tyreSpeed *= aeroKit.getStraightLineSpeedMultiplier();
        }
        
        return tyreSpeed * weather.speedReduction;
    }
    
    public double getFuelConsumptionPerLap(Track track, WeatherCondition weather) {
        double baseFuel = 2.5;
        double engineFuel = baseFuel / engine.getFuelEfficiency();
        double aeroFuel = engineFuel * aeroKit.getPackage().fuelConsumption;
        double trackFuel = aeroFuel * track.getFuelMultiplier();
        
        return trackFuel * weather.fuelConsumptionIncrease;
    }
    
    public double getTyreWearPerLap(Track track, WeatherCondition weather) {
        double baseWear = 2.0;
        double trackWear = baseWear * track.getTyreWearMultiplier();
        double tyreWear = trackWear / tyres.getType().durabilityMultiplier;
        
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