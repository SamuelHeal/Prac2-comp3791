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
        
        // Start-of-race acceleration bonus
        double accel = car.getAcceleration();
        double startBonus = Math.max(0, (6.0 - accel) * 0.5); // 0.5s per 0.1s below 6.0s
        startBonus = Math.min(startBonus, 1.0); // cap at 1.0s
        totalTime -= startBonus;
        
        for (int lap = 1; lap <= track.getTotalLaps(); lap++) {
            // Check if pit stop is needed
            boolean needsFuel = currentFuel < fuelPerLap * 2;
            boolean needsTyres = simulatedTyres.getCurrentWear() > 80.0;
            
            if (needsFuel || needsTyres) {
                double fuelToAdd = car.getMaxFuelCapacity() - currentFuel;
                TyreType newTyres = needsTyres ? selectOptimalTyre(track, weather, lap) : null;
                
                PitStop pitStop = new PitStop(lap, needsTyres, newTyres, fuelToAdd, car.getAcceleration());
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
        double lapTime = baseTime * (100.0 / speed);
        
        // Factor in acceleration (lower is better), effect depends on weather
        double acceleration = car.getAcceleration(); // 0-100 km/h time in seconds
        double baseline = 4.0; // F1-level acceleration
        double accelEffectPerSec;
        switch (weather) {
            case DRY:
                accelEffectPerSec = 0.02; // 2% per second
                break;
            case WET:
                accelEffectPerSec = 0.03; // 3% per second
                break;
            case EXTREME_WET:
                accelEffectPerSec = 0.04; // 4% per second
                break;
            default:
                accelEffectPerSec = 0.02;
        }
        double accelEffect = (acceleration - baseline) * accelEffectPerSec;
        lapTime *= (1.0 + accelEffect);
        
        return lapTime;
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