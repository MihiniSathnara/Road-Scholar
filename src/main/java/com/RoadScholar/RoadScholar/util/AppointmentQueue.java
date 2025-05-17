package com.RoadScholar.RoadScholar.util;

import com.RoadScholar.RoadScholar.model.Appointment;
import java.util.LinkedList;
import java.util.List;

public class AppointmentQueue<T> {
    private List<T> elements;

    public AppointmentQueue(){
        elements=new LinkedList<>();
    }
    public void enqueue(T item){
        elements.add(item);
    }
    public T dequeue(){
        if(isEmpty()) return null;
        return  elements.remove(0);
    }

    public T peek(){
        if(isEmpty()) return null;
        return elements.get(0);
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public int size(){
        return elements.size();
    }

    public List<T> getAllElements(){
        return new LinkedList<>(elements);
    }

}
