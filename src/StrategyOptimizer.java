// Strategy Optimizer
class StrategyOptimizer {
    
    public RaceStrategy optimizeStrategy(RaceCar car, Track track, WeatherCondition weather) {
        RaceStrategy strategy = new RaceStrategy();
        
        // Simulate race to determine optimal pit stops
        double currentFuel = car.getCurrentFuel();
        Tyres simulatedTyres = new Tyres(car.getTyres().getType());
        double totalTime = 0.0;
        
        double fuelPerLap = car.getFuelConsumptionPerLap(track, weather);
        double wearPerLap = car.getTyreWearPerLap(track, weather);
        double lapTime = calculateLapTime(car, track, weather);
        
        for (int lap = 1; lap <= track.getTotalLaps(); lap++) {
            // Check if pit stop is needed
            boolean needsFuel = currentFuel < fuelPerLap * 2;
            boolean needsTyres = simulatedTyres.getCurrentWear() > 80.0;
            
            if (needsFuel || needsTyres) {
                double fuelToAdd = car.getMaxFuelCapacity() - currentFuel;
                TyreType newTyres = needsTyres ? selectOptimalTyre(track, weather, lap) : null;
                
                PitStop pitStop = new PitStop(lap, needsTyres, newTyres, fuelToAdd);
                strategy.addPitStop(pitStop);
                
                currentFuel = car.getMaxFuelCapacity();
                if (needsTyres) {
                    simulatedTyres = new Tyres(newTyres);
                }
                
                totalTime += pitStop.getTimeDelay();
            }
            
            // Simulate lap
            currentFuel -= fuelPerLap;
            simulatedTyres.addWear(wearPerLap);
            totalTime += lapTime;
            
            // Check for fuel emergency
            if (currentFuel < 0) {
                strategy.setValid(false);
                break;
            }
        }
        
        strategy.setTotalTime(totalTime);
        return strategy;
    }
    
    private double calculateLapTime(RaceCar car, Track track, WeatherCondition weather) {
        double baseTime = 90.0; // 90 seconds base lap time
        double speed = car.getOverallSpeed(track, weather);
        return baseTime * (100.0 / speed);
    }
    
    private TyreType selectOptimalTyre(Track track, WeatherCondition weather, int currentLap) {
        if (weather == WeatherCondition.WET || weather == WeatherCondition.EXTREME_WET) {
            return TyreType.SOFT; // Better grip in wet conditions
        } else if (track.getType().equals("Oval")) {
            return TyreType.HARD; // Better for high-speed circuits
        } else {
            return TyreType.MEDIUM; // Balanced choice
        }
    }
}