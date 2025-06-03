# Practical 2 - Comp3791

## Intitial Prompt

```
ROLE:
You are an expert Java programmer, tasked with completing a race team management simulation.

TASK:
The system allows users to customise race cars with different engines, tyres, and aerodynamic features. It also includes a strategy optimiser to plan pit stops, fuel usage, and tyre changes based on race conditions such as track type, weather, and distance.

KEY FEATURES:
Car Customisation: Users can select and upgrade components like engines (e.g., standard, turbocharged), tyres (soft, medium, hard), and aerodynamic kits*(see page 3). Each choice affects performance metrics such as speed, fuel efficiency, and handling.

Race Strategy Optimisation: Users can input race details (track length, weather, fuel capacity, etc.) to generate an optimal pit stop and tyre change strategy for a minimum of 5 race tracks. Your program should take into account factors such as wear rates, and fuel consumption based on the selected car components.

We will customise further after the initial setup
```

## Prompt 2:

```
The code provided is missing the required imports. Please add these to the code.
```

## Prompt 3:

```
Replace the Aerodynamic kits currently in the project with the following options:

1. Standard Kit (Basic Aerodynamics)
Description:
Includes basic front and rear spoilers to reduce drag and provide minimal downforce.
Performance Impact:
Suitable for general-purpose tracks; balances speed and stability without extreme
effects.

2. Downforce-Focused Kit
Description:
Features large front splitters and a prominent rear wing to maximise downforce.
Performance Impact:
Improves cornering ability and traction, especially on twisty tracks, but increases
drag and reduces top speed.

3. Low-Drag Kit
Description:
Designed with sleek, minimal features to reduce air resistance (e.g., smaller spoilers
or absence of a rear wing).
Performance Impact:
Prioritises top speed on long straight tracks but sacrifices cornering stability.

4. Adjustable Aero Kit
Description:
Equipped with adjustable components like movable wings or variable angle splitters.
Performance Impact:
Allows for on-the-fly adjustments to suit different sections of a track-low downforce
for straights and high downforce for corners.

5. Ground Effect Kit
Description:
Includes components like underbody diffusers and side skirts to channel airflow
underneath the car, creating suction to stick the car to the track.
Performance Impact:
Increases downforce significantly without adding much drag, making it ideal for high-
speed tracks.

6. Drag Reduction System (DRS) Kit
Description:
Features a mechanism to temporarily reduce drag by altering aerodynamic components,
commonly seen in Formula 1 cars.
Performance Impact:
Allows for better overtaking by boosting straight-line speed when activated.

7. Wet Weather Kit
Description:
Designed for races in wet conditions, with features like extended wheel covers or
deflectors to minimise water spray and improve traction.
Performance Impact:
Enhances stability and visibility in rainy conditions, sacrificing some top speed.

8. Hybrid Kit
Description:
Combines elements of low-drag and downforce-focused kits to achieve a middle ground
between speed and stability.
Performance Impact:
Offers versatility for tracks with a mix of straights and corners.

9. Extreme Aero Kit
Description:
Pushes the limits of aerodynamics with aggressive features like oversized wings,
massive splitters, and large diffusers
Performance Impact:
Maximises downforce at the cost of significant drag, ideal for short, technical circuits.
```

Prompt 4:

```
Reformat the console display for the Aerodynamic Kits to be in an easy to read table format, as the current list based format is frustrating to scroll through. You do not need to include the description or performance impact text. Only include the performance metrics associated to each kit.
```
