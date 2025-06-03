enum AeroPackage {
    STANDARD_KIT(1.0, 1.0, 1.0, "Basic Aerodynamics", 
                 "Includes basic front and rear spoilers to reduce drag and provide minimal downforce. Suitable for general-purpose tracks; balances speed and stability without extreme effects."),
    
    DOWNFORCE_FOCUSED(0.85, 1.3, 0.75, "Maximum Downforce", 
                      "Features large front splitters and a prominent rear wing to maximise downforce. Improves cornering ability and traction, especially on twisty tracks, but increases drag and reduces top speed."),
    
    LOW_DRAG(1.2, 0.8, 1.3, "Minimal Air Resistance", 
             "Designed with sleek, minimal features to reduce air resistance (e.g., smaller spoilers or absence of a rear wing). Prioritises top speed on long straight tracks but sacrifices cornering stability."),
    
    ADJUSTABLE_AERO(1.05, 1.05, 0.95, "Adaptive Components", 
                    "Equipped with adjustable components like movable wings or variable angle splitters. Allows for on-the-fly adjustments to suit different sections of a track-low downforce for straights and high downforce for corners."),
    
    GROUND_EFFECT(1.0, 1.25, 0.9, "Underbody Aerodynamics", 
                  "Includes components like underbody diffusers and side skirts to channel airflow underneath the car, creating suction to stick the car to the track. Increases downforce significantly without adding much drag, making it ideal for high-speed tracks."),
    
    DRS_KIT(1.15, 0.95, 1.1, "Drag Reduction System", 
            "Features a mechanism to temporarily reduce drag by altering aerodynamic components, commonly seen in Formula 1 cars. Allows for better overtaking by boosting straight-line speed when activated."),
    
    WET_WEATHER(0.9, 1.1, 0.85, "Rain Optimized", 
                "Designed for races in wet conditions, with features like extended wheel covers or deflectors to minimise water spray and improve traction. Enhances stability and visibility in rainy conditions, sacrificing some top speed."),
    
    HYBRID(1.05, 1.1, 0.95, "Balanced Performance", 
           "Combines elements of low-drag and downforce-focused kits to achieve a middle ground between speed and stability. Offers versatility for tracks with a mix of straights and corners."),
    
    EXTREME_AERO(0.8, 1.4, 0.7, "Maximum Downforce", 
                 "Pushes the limits of aerodynamics with aggressive features like oversized wings, massive splitters, and large diffusers. Maximises downforce at the cost of significant drag, ideal for short, technical circuits.");

    public final double straightLineSpeed;
    public final double corneringSpeed;
    public final double fuelConsumption;
    public final String displayName;
    public final String description;
    
    AeroPackage(double straight, double cornering, double fuel, String name, String desc) {
        this.straightLineSpeed = straight;
        this.corneringSpeed = cornering;
        this.fuelConsumption = fuel;
        this.displayName = name;
        this.description = desc;
    }
}