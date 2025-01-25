package Entities;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Client.class)
public class Client_ {
    public static volatile SingularAttribute<Client, Integer> id;
    public static volatile SingularAttribute<Client, String> name;
    public static volatile SingularAttribute<Client, String> passportData;
}