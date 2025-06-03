import java.util.*;

public class RaceTeamManagement {
    private List<RaceCar> cars;
    private List<Track> tracks;
    private StrategyOptimizer optimizer;
    private Scanner scanner;
    
    public RaceTeamManagement() {
        this.cars = new ArrayList<>();
        this.tracks = initializeTracks();
        this.optimizer = new StrategyOptimizer();
        this.scanner = new Scanner(System.in);
    }
    
    private List<Track> initializeTracks() {
        List<Track> trackList = new ArrayList<>();
        trackList.add(new Track("Monaco Grand Prix", "Street", 78, 3.337, 1.1, 1.3));
        trackList.add(new Track("Silverstone Circuit", "Road", 52, 5.891, 1.0, 1.0));
        trackList.add(new Track("Monza Circuit", "Road", 53, 5.793, 1.2, 0.8));
        trackList.add(new Track("Indianapolis 500", "Oval", 200, 4.023, 1.3, 1.1));
        trackList.add(new Track("Nürburgring", "Road", 67, 5.148, 1.1, 1.2));
        trackList.add(new Track("Suzuka Circuit", "Road", 53, 5.807, 1.0, 1.1));
        return trackList;
    }
    
    public void createCar() {
        System.out.print("Enter car name: ");
        String name = scanner.nextLine();
        RaceCar car = new RaceCar(name);
        cars.add(car);
        System.out.println("Car '" + name + "' created successfully!");
    }
    
    public void customizeCar() {
        if (cars.isEmpty()) {
            System.out.println("No cars available. Create a car first.");
            return;
        }
        
        displayCars();
        System.out.print("Select car index: ");
        int carIndex = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        if (carIndex < 0 || carIndex >= cars.size()) {
            System.out.println("Invalid car selection.");
            return;
        }
        
        RaceCar car = cars.get(carIndex);
        
        System.out.println("\nCustomization Options:");
        System.out.println("1. Engine");
        System.out.println("2. Tyres");
        System.out.println("3. Aerodynamics");
        System.out.print("Choose component to customize: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                customizeEngine(car);
                break;
            case 2:
                customizeTyres(car);
                break;
            case 3:
                customizeAero(car);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void customizeEngine(RaceCar car) {
        System.out.println("\nEngine Options:");
        System.out.println("1. STANDARD (Speed: 1.0x, Fuel Eff: 1.0x)");
        System.out.println("2. TURBOCHARGED (Speed: 1.3x, Fuel Eff: 0.8x)");
        System.out.println("3. HYBRID (Speed: 1.1x, Fuel Eff: 1.2x)");
        System.out.print("Select engine: ");
        
        int choice = scanner.nextInt();
        EngineType[] engines = EngineType.values();
        if (choice > 0 && choice <= engines.length) {
            car.customizeEngine(engines[choice - 1]);
            System.out.println("Engine updated to " + engines[choice - 1]);
        }
    }
    
    private void customizeTyres(RaceCar car) {
        System.out.println("\nTyre Options:");
        System.out.println("1. SOFT (Grip: 1.2x, Durability: 0.7x, Max Laps: 25)");
        System.out.println("2. MEDIUM (Grip: 1.0x, Durability: 1.0x, Max Laps: 40)");
        System.out.println("3. HARD (Grip: 0.9x, Durability: 1.3x, Max Laps: 60)");
        System.out.print("Select tyres: ");
        
        int choice = scanner.nextInt();
        TyreType[] tyres = TyreType.values();
        if (choice > 0 && choice <= tyres.length) {
            car.customizeTyres(tyres[choice - 1]);
            System.out.println("Tyres updated to " + tyres[choice - 1]);
        }
    }
    
    private void customizeAero(RaceCar car) {
        System.out.println("\nAerodynamic Options:");
        System.out.println("1. LOW_DOWNFORCE (Straight: 1.1x, Cornering: 0.9x)");
        System.out.println("2. BALANCED (Straight: 1.0x, Cornering: 1.0x)");
        System.out.println("3. HIGH_DOWNFORCE (Straight: 0.9x, Cornering: 1.2x)");
        System.out.print("Select aero package: ");
        
        int choice = scanner.nextInt();
        AeroPackage[] aeros = AeroPackage.values();
        if (choice > 0 && choice <= aeros.length) {
            car.customizeAero(aeros[choice - 1]);
            System.out.println("Aerodynamics updated to " + aeros[choice - 1]);
        }
    }
    
    public void optimizeStrategy() {
        if (cars.isEmpty()) {
            System.out.println("No cars available. Create a car first.");
            return;
        }
        
        displayCars();
        System.out.print("Select car index: ");
        int carIndex = scanner.nextInt();
        
        if (carIndex < 0 || carIndex >= cars.size()) {
            System.out.println("Invalid car selection.");
            return;
        }
        
        displayTracks();
        System.out.print("Select track index: ");
        int trackIndex = scanner.nextInt();
        
        if (trackIndex < 0 || trackIndex >= tracks.size()) {
            System.out.println("Invalid track selection.");
            return;
        }
        
        System.out.println("\nWeather Conditions:");
        System.out.println("1. DRY");
        System.out.println("2. WET");
        System.out.println("3. EXTREME_WET");
        System.out.print("Select weather: ");
        int weatherChoice = scanner.nextInt();
        
        WeatherCondition[] conditions = WeatherCondition.values();
        if (weatherChoice < 1 || weatherChoice > conditions.length) {
            System.out.println("Invalid weather selection.");
            return;
        }
        
        RaceCar car = cars.get(carIndex);
        Track track = tracks.get(trackIndex);
        WeatherCondition weather = conditions[weatherChoice - 1];
        
        RaceStrategy strategy = optimizer.optimizeStrategy(car, track, weather);
        displayStrategy(car, track, weather, strategy);
    }
    
    private void displayStrategy(RaceCar car, Track track, WeatherCondition weather, RaceStrategy strategy) {
        System.out.println("\n=== RACE STRATEGY OPTIMIZATION ===");
        System.out.println("Car: " + car.getName());
        System.out.println("Track: " + track.getName() + " (" + track.getTotalLaps() + " laps)");
        System.out.println("Weather: " + weather);
        System.out.println("Strategy Valid: " + (strategy.isValid() ? "YES" : "NO"));
        System.out.printf("Total Race Time: %.2f seconds (%.2f minutes)\n", 
                         strategy.getTotalTime(), strategy.getTotalTime() / 60);
        
        System.out.println("\nPit Stop Strategy:");
        if (strategy.getPitStops().isEmpty()) {
            System.out.println("No pit stops required!");
        } else {
            for (int i = 0; i < strategy.getPitStops().size(); i++) {
                PitStop stop = strategy.getPitStops().get(i);
                System.out.printf("Stop %d - Lap %d: ", i + 1, stop.getLap());
                if (stop.isChangeTyres()) {
                    System.out.printf("Tyre change to %s, ", stop.getNewTyreType());
                }
                System.out.printf("Fuel: %.1f L, Time: %.1f sec\n", 
                                 stop.getFuelAmount(), stop.getTimeDelay());
            }
        }
        
        System.out.printf("\nRace Performance Metrics:\n");
        System.out.printf("Speed Rating: %.1f\n", car.getOverallSpeed(track, weather));
        System.out.printf("Fuel per Lap: %.2f L\n", car.getFuelConsumptionPerLap(track, weather));
        System.out.printf("Tyre Wear per Lap: %.2f%%\n", car.getTyreWearPerLap(track, weather));
    }
    
    private void displayCars() {
        System.out.println("\nAvailable Cars:");
        for (int i = 0; i < cars.size(); i++) {
            RaceCar car = cars.get(i);
            System.out.printf("%d. %s (Engine: %s, Tyres: %s, Aero: %s)\n", 
                             i, car.getName(), car.getEngine().getType(), 
                             car.getTyres().getType(), car.getAeroKit().getPackage());
        }
    }
    
    private void displayTracks() {
        System.out.println("\nAvailable Tracks:");
        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            System.out.printf("%d. %s (%s, %d laps, %.3f km)\n", 
                             i, track.getName(), track.getType(), 
                             track.getTotalLaps(), track.getLapLength());
        }
    }
    
    public void displayMenu() {
        System.out.println("\n=== RACE TEAM MANAGEMENT SYSTEM ===");
        System.out.println("1. Create New Car");
        System.out.println("2. Customize Car");
        System.out.println("3. Optimize Race Strategy");
        System.out.println("4. Display Cars");
        System.out.println("5. Display Tracks");
        System.out.println("6. Exit");
        System.out.print("Choose option: ");
    }
    
    public void run() {
        System.out.println("Welcome to the Race Team Management System!");
        
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    createCar();
                    break;
                case 2:
                    customizeCar();
                    break;
                case 3:
                    optimizeStrategy();
                    break;
                case 4:
                    displayCars();
                    break;
                case 5:
                    displayTracks();
                    break;
                case 6:
                    System.out.println("Thank you for using the Race Team Management System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    public static void main(String[] args) {
        RaceTeamManagement system = new RaceTeamManagement();
        system.run();
    }
}