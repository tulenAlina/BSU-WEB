package Entities;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Request.class)
public class Request_ {
    public static volatile SingularAttribute<Request, Integer> id;
    public static volatile SingularAttribute<Request, Client> client;
    public static volatile SingularAttribute<Request, Car> car;
    public static volatile SingularAttribute<Request, Integer> rentalPeriod;
}