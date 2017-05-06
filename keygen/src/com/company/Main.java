package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Keygen keygen = new Keygen(args);
        keygen.saveKey();

    }
}
