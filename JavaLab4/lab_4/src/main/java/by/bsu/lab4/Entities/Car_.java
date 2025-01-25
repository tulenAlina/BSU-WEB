package by.bsu.lab4.Entities;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Car.class)
public class Car_ {
    public static volatile SingularAttribute<Car, Integer> id;
    public static volatile SingularAttribute<Car, String> model;
    public static volatile SingularAttribute<Car, String> licensePlate;
    public static volatile SingularAttribute<Car, String> status;
}