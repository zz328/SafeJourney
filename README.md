# SafeJourney

Safe Journey - App prototype, completed as a part of HopHacks Fall 2019
TEAM MEMBERS: Keilani Caruso, Jason Kurlander, David Skaff, Emily Zeng

What is our project?
The purpose of Safe Journey is to provide its users with a navigation system
that factors police crime data into its algorithm to navigate away from
dangerous streets. It maps the shortest and safest route from point A to
point B. Police reports update the algorithm with current crime information
to constantly adjust and improve its pathing.

Why our project?
We want our project to help Baltimore residents stay safe in their day to
day life. Kids can walk to school on safer paths.

USAGE
Currently, this application is based entirely within your terminal. Compile
every .java file. Run StreetSearch with the following arguments: mapfile,
start Latitude,Longitude pair, and end Latitude,Longitude pair.

example:
$ java graphFiles.StreetSearch graphFiles/baltimore.txt -76.6063,39.2907
-76.6136,39.3195

FUTURE GOALS
-Planning to build app with gui
-updating info and filtering it based on type of crime more thoroughly
-being more precise/predictive in the severity of danger in various areas
-give more detailed directions with a focus on accessibility for all users
-Implement more efficient A* shortest path algorithm
