package com.example.roommatefinder.singletone;

import androidx.navigation.NavController;

public class GetNavController {
    public  static NavController navController;

    public GetNavController(NavController navController) {
        this.navController=navController;
    }

    public static NavController getNavController() {
        return navController;
    }
}
