package com.example.planetzeapp;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnualQs_Finish extends QFragment {
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.annual_finish, container, false);
        Button startButton = view.findViewById(R.id.viewResultsButton);
        startButton.setOnClickListener(v -> {
            annual_info();
            navigateToQFragment();        });
        return view;

    }
    private void navigateToQFragment() {
        Temp t = new Temp();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, t);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void annual_info() {
        if (uid == null) {
            Log.e("FirebaseSave", "User is not signed in.");
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(uid).child("annual");
        Map<String, Object> annualData = new HashMap<>();

        Map<String, Object> transportationData = new HashMap<>();
        if(questions.get(0).getUserAnswer().equals("True"))
        {
            transportationData.put("uses_car", true); // Q0
            transportationData.put("car_type", questions.get(1).getUserAnswer()); // Q1
            transportationData.put("distance", questions.get(2).getUserAnswer()); // Q2
        }
        else
        {
            transportationData.put("uses_car", false); // Q0
            transportationData.put("car_type", 0); // Q1
            transportationData.put("distance", 0); // Q2
        }
        transportationData.put("public_transit_use", questions.get(3).getUserAnswer()); // Q3
        transportationData.put("hours_on_public_transit", questions.get(4).getUserAnswer()); // Q4
        transportationData.put("num_short_flights", questions.get(5).getUserAnswer()); // Q5
        transportationData.put("num_long_flights", questions.get(6).getUserAnswer()); // Q6

        int transportationCo2e = transport_calc();
        transportationData.put("transportation_co2e", transportationCo2e);

        Map<String, Object> foodData = new HashMap<>();
        foodData.put("diet_type", questions.get(7).getUserAnswer()); // Q7
        if(questions.get(7).getUserAnswer().equals("Meat-based (eat all types of animal products)")) {
            foodData.put("beef_frequency", questions.get(8).getUserAnswer()); // Q8
            foodData.put("pork_frequency", questions.get(9).getUserAnswer()); // Q9
            foodData.put("chicken_frequency", questions.get(10).getUserAnswer()); // Q10
            foodData.put("fish_frequency", questions.get(11).getUserAnswer()); // Q11
        }
        else {
            foodData.put("beef_frequency", 0); // Q8
            foodData.put("pork_frequency", 0); // Q9
            foodData.put("chicken_frequency", 0); // Q10
            foodData.put("fish_frequency", 0); // Q11
        }
        foodData.put("food_waste_frequency", questions.get(12).getUserAnswer()); // Q12
        int foodCo2e = food_calc();
        foodData.put("food_co2e", foodCo2e);
        ref.setValue(foodData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseSave", "Food data saved successfully for user: " + uid);
                    } else {
                        Log.e("FirebaseSave", "Failed to save food data for user: " + uid, task.getException());
                    }
                });

        Map<String, Object> houseData = new HashMap<>();
        houseData.put("structure_type", questions.get(13).getUserAnswer()); // Q13
        houseData.put("household_members", questions.get(14).getUserAnswer()); // Q14
        houseData.put("size", questions.get(15).getUserAnswer()); // Q15
        houseData.put("heating_source", questions.get(16).getUserAnswer()); // Q16
        houseData.put("bill", questions.get(17).getUserAnswer()); // Q17
        houseData.put("water_source", questions.get(18).getUserAnswer()); // Q18
        houseData.put("renewable_energy_uasge", questions.get(19).getUserAnswer()); // Q19
        int housingCo2e = house_calc();
        houseData.put("house_co2e", housingCo2e);
        ref.setValue(houseData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseSave", "Housing data saved successfully for user: " + uid);
                    } else {
                        Log.e("FirebaseSave", "Failed to save housing data for user: " + uid, task.getException());
                    }
                });

        Map<String, Object> consumptionData = new HashMap<>();
        consumptionData.put("clothing_frequency", questions.get(20).getUserAnswer()); // Q20
        consumptionData.put("eco_friendly_frequency", questions.get(21).getUserAnswer()); // Q21
        consumptionData.put("device_frequency", questions.get(22).getUserAnswer()); // Q22
        consumptionData.put("recycling_frequency", questions.get(23).getUserAnswer()); // Q23
        int consumptionCo2e = cons_calc();
        consumptionData.put("consumption_co2e", consumptionCo2e);
        ref.setValue(consumptionData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseSave", "food data saved successfully for user: " + uid);
                    } else {
                        Log.e("FirebaseSave", "Failed to save food data for user: " + uid, task.getException());
                    }
                });

        annualData.put("transportation", transportationData);
        annualData.put("food", foodData);
        annualData.put("housing", houseData);
        annualData.put("consumption", consumptionData);
        ref.setValue(annualData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseSave", "Annual data saved successfully for user: " + uid);
                    } else {
                        Log.e("FirebaseSave", "Failed to save annual data for user: " + uid, task.getException());
                    }
                });

    }




    private int transport_calc() {
        int co2e = 0;
        return co2e;
    }
    private int food_calc() {
        int co2e = 0;
        return co2e;
    }
    private int cons_calc() {
        int co2e = 0;
        return co2e;
    }private int house_calc() {
        int co2e = 0;
        return co2e;
    }




}