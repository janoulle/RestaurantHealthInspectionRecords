# RestaurantHealthInspectionRecords
Update to my first android app which displays latest inspection results for counties in NorthEast GA


This is very much a work in progress. I am hoping to use this as a opportunity to re-learn Android and best practices. Some significant changes from the first version of the app include:

1. Using a realtime database (Firebase) instead of an SQLite database. This is a Google product that has some nice support for Android baked in already! This solves an earlier problem I had where I needed a way to ensure regular updates to the application database. Now, an update to the data doesn't require an app update.
2. Learning best practices for managing actions that could take a while to resolve e.g. geocoding an address. Previously, I was doing a lot of heavy lifting on the UI thread and in this incarnation of the app, I'll be making use of IntentServices.
3. Switch to gradle based way of development using Android Studio (previous version of the app was built using Eclipse)
4. Incorporate best practices on release management of the application. 
5. Incorporate best practices on capturing metrics related to exceptional events within the app. Firebase and Fabric are both Google products that I'm leveraging for this.
6. Incorporate best practices on managing keys and secrets. Previously, I was hardcoding the aforementioned items directly within the Java code and this time, taking advantage of gradle and learning to love my .gitignore file. Eventually, I'd like to learn more about using Proguard.
7. There are no tests currently. Try not to judge me too harshly. 
