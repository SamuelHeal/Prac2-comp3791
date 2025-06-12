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
            // Use the new function to check for low fuel and trigger pit stop
            PitStop fuelPitStop = checkAndTriggerPitStopForLowFuel(car, lap, track, weather);
            boolean needsTyres = simulatedTyres.getCurrentWear() > 80.0;
            PitStop tyrePitStop = null;
            if (needsTyres) {
                double fuelToAdd = car.getMaxFuelCapacity() - currentFuel;
                TyreType newTyres = selectOptimalTyre(track, weather, lap);
                tyrePitStop = new PitStop(lap, true, newTyres, fuelToAdd, car.getAcceleration());
            }

            // If both are needed, combine into one pit stop (change tyres and refuel)
            if (fuelPitStop != null && needsTyres) {
                // Combine both actions into one pit stop
                double fuelToAdd = car.getMaxFuelCapacity() - currentFuel;
                TyreType newTyres = selectOptimalTyre(track, weather, lap);
                PitStop combinedPitStop = new PitStop(lap, true, newTyres, fuelToAdd, car.getAcceleration());
                strategy.addPitStop(combinedPitStop);
                currentFuel = car.getMaxFuelCapacity();
                simulatedTyres = new Tyres(newTyres);
                totalTime += combinedPitStop.getTimeDelay();
            } else if (fuelPitStop != null) {
                strategy.addPitStop(fuelPitStop);
                currentFuel = car.getMaxFuelCapacity();
                totalTime += fuelPitStop.getTimeDelay();
            } else if (needsTyres) {
                strategy.addPitStop(tyrePitStop);
                simulatedTyres = new Tyres(tyrePitStop.getNewTyreType());
                totalTime += tyrePitStop.getTimeDelay();
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
        
        // New: Factor in cornering (higher cornering = faster lap)
        double cornering = car.getCornering();
        // Assume a reference cornering value (e.g., 10.0 is "excellent"), scale effect up to 10% faster for top cornering
        double referenceCornering = 10.0;
        double corneringEffect = Math.max(0.8, 1.0 - (cornering / referenceCornering) * 0.1); // up to 10% reduction
        lapTime *= corneringEffect;
        
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
    
    /**
     * Checks the fuel tank info and triggers a pit stop if fuel is below the threshold.
     * @param car The RaceCar to check.
     * @param currentLap The current lap number.
     * @param track The Track (for fuel calculation).
     * @param weather The WeatherCondition (for fuel calculation).
     * @param fuelThreshold The threshold (0.0-1.0) below which a pit stop is triggered (default 0.2 for 20%).
     * @return A PitStop if fuel is low, otherwise null.
     */
    public static PitStop checkAndTriggerPitStopForLowFuel(RaceCar car, int currentLap, Track track, WeatherCondition weather, double fuelThreshold) {
        System.out.println(car.getFuelTankInfo());
        double currentFuel = car.getCurrentFuel();
        double maxFuel = car.getMaxFuelCapacity();
        if (currentFuel <= maxFuel * fuelThreshold) {
            double fuelToAdd = maxFuel - currentFuel;
            // No tyre change, so pass false and null for tyres
            return new PitStop(currentLap, false, null, fuelToAdd, car.getAcceleration());
        }
        return null;
    }
    // Overload with default threshold
    public static PitStop checkAndTriggerPitStopForLowFuel(RaceCar car, int currentLap, Track track, WeatherCondition weather) {
        return checkAndTriggerPitStopForLowFuel(car, currentLap, track, weather, 0.2);
    }
}