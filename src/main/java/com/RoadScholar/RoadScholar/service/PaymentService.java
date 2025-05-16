package com.RoadScholar.RoadScholar.service;

import com.RoadScholar.RoadScholar.model.Payment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.RoadScholar.RoadScholar.storage.FileStorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final String FILE_PATH="data/payments.json";
    private final FileStorageHandler fileStorageHandler;

    @Autowired
    public PaymentService(FileStorageHandler fileStorageHandler){
        this.fileStorageHandler=fileStorageHandler;
    }

    public List<Payment> getAllPayments(){
        return fileStorageHandler.readFromFile(FILE_PATH, new TypeReference<List<Payment>>() {});
    }

    public void addPayment(Payment payment){
        List<Payment> payments=getAllPayments();
        payments.add(payment);
        fileStorageHandler.writeToFile(FILE_PATH, payments);
    }

    public List<Payment> getPaymentByStudentId(String studentId){
        return getAllPayments().stream()
                .filter(p -> p.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }
    public List<Payment> getUnverifiedPayments(){
        return getAllPayments().stream()
                .filter(payment -> !payment.isPaymentVerified())
                .collect(Collectors.toList());
    }

    public void verifyPayment(String paymentId){
        List<Payment> payments=getAllPayments();
        for(Payment payment: payments){
            if(payment.getPaymentId().equals(paymentId)){
                payment.setPaymentVerified(true);
                break;
            }
        }
        fileStorageHandler.writeToFile(FILE_PATH, payments);
    }
}
