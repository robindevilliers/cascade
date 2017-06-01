package com.github.robindevilliers.onlinebankingexample;


import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class RandomNumberService {

    Random randomGenerator = new Random();

    public int getRandomNumber(){
        return Math.abs(randomGenerator.nextInt());
    }

}
