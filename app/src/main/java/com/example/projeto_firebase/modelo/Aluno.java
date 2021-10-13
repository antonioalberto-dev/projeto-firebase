package com.example.projeto_firebase.modelo;

import java.util.Comparator;

public class Aluno implements Comparable<Aluno>{
    private String uid;
    private String name;
    private String registration;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Aluno o) {
        return this.name.compareTo(o.name);
    }
}
