enum AeroPackage {
    STANDARD_AERODYNAMICS(0.35, 150, 320, 12.0, 5, 
                         "Includes basic front and rear spoilers to reduce drag and provide minimal downforce. Suitable for general-purpose tracks; balances speed and stability without extreme effects."),
    
    DOWNFORCE_FOCUSED(0.45, 300, 290, 10.0, 9, 
                      "Features large front splitters and a prominent rear wing to maximise downforce. Improves cornering ability and traction, especially on twisty tracks, but increases drag and reduces top speed."),
    
    LOW_DRAG_PACKAGE(0.25, 80, 350, 15.0, 3, 
                     "Designed with sleek, minimal features to reduce air resistance (e.g., smaller spoilers or absence of a rear wing). Prioritises top speed on long straight tracks but sacrifices cornering stability."),
    
    ADJUSTABLE_AERO(0.33, 170, 325, 13.0, 6, 
                    "Equipped with adjustable components like movable wings or variable angle splitters. Allows for on-the-fly adjustments to suit different sections of a track-low downforce for straights and high downforce for corners."),
    
    GROUND_EFFECT(0.32, 250, 330, 12.5, 8, 
                  "Includes components like underbody diffusers and side skirts to channel airflow underneath the car, creating suction to stick the car to the track. Increases downforce significantly without adding much drag, making it ideal for high-speed tracks."),
    
    DRS_SYSTEM(0.28, 120, 345, 14.0, 4, 
               "Features a mechanism to temporarily reduce drag by altering aerodynamic components, commonly seen in Formula 1 cars. Allows for better overtaking by boosting straight-line speed when activated."),
    
    WET_WEATHER_PACKAGE(0.38, 180, 310, 11.0, 7, 
                        "Designed for races in wet conditions, with features like extended wheel covers or deflectors to minimise water spray and improve traction. Enhances stability and visibility in rainy conditions, sacrificing some top speed."),
    
    HYBRID_AERODYNAMICS(0.31, 190, 330, 13.5, 6, 
                        "Combines elements of low-drag and downforce-focused kits to achieve a middle ground between speed and stability. Offers versatility for tracks with a mix of straights and corners."),
    
    EXTREME_DOWNFORCE(0.55, 400, 270, 8.0, 10, 
                      "Pushes the limits of aerodynamics with aggressive features like oversized wings, massive splitters, and large diffusers. Maximises downforce at the cost of significant drag, ideal for short, technical circuits.");

    public final double dragCoefficient;     // Cd value
    public final int downforce;              // kg
    public final int topSpeed;               // km/h
    public final double fuelEfficiency;      // km/l
    public final int corneringAbility;       // rating out of 10
    public final String description;
    
    AeroPackage(double cd, int downforceKg, int topSpeedKmh, double fuelEffKml, int cornering, String desc) {
        this.dragCoefficient = cd;
        this.downforce = downforceKg;
        this.topSpeed = topSpeedKmh;
        this.fuelEfficiency = fuelEffKml;
        this.corneringAbility = cornering;
        this.description = desc;
    }
    
    // Helper method to get display name from enum name
    public String getDisplayName() {
        return this.name().replace("_", " ");
    }
}