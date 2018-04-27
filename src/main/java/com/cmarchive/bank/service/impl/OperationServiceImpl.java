package com.cmarchive.bank.service.impl;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.exceptions.OperationNotFoundException;
import com.cmarchive.bank.repository.OperationRepository;
import com.cmarchive.bank.service.OperationService;

@Service
public class OperationServiceImpl implements OperationService {

    private OperationRepository operationRepository;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository) {
        super();
        this.operationRepository = operationRepository;
    }
    
    @Override
    public List<Operation> list() {
        return operationRepository.findAllByOrderByDateOperationDescIntituleAsc();
    }
    
    @Override
    public Operation get(long id) {
        Assert.notNull(id, "Id cannot be null");
        Operation operation = operationRepository.findOne(id);
        
        if (operation == null) {
            throw new OperationNotFoundException("Operation with id " + id + " not found.");
        }
        
        return operation;
    }
    
    @Override
    public Operation save(Operation operation) {
        return operationRepository.save(operation);
    }
    
    @Override
    public void delete(Long id) {
        operationRepository.delete(id);
    }
    
    @Override
    public List<Operation> findByUser(Long idUtilisateur) {
        return operationRepository.findByUserId(idUtilisateur);
    }
    
    @Override
    public Operation addOpPermanenteToOperations(int month, PermanentOperation permanentOperation) {
        User user = permanentOperation.getUser();
        
        LocalDate localDate = LocalDate.of(Year.now().getValue(), month, permanentOperation.getJour());

        Operation operation = new Operation();
        operation.setDateOperation(localDate);
        operation.setIntitule(permanentOperation.getIntitule());
        operation.setPrix(permanentOperation.getPrix());
        operation.setTypeOperation(permanentOperation.getTypeOperation());
        operation.setUser(user);
        operation.setPermanentOperation(permanentOperation);
        
        return operationRepository.save(operation);
    }
    
    @Override
    public List<Operation> findByMonth(Long id, int month) {
        LocalDate start = LocalDate.now().withMonth(month).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = LocalDate.now().withMonth(month).with(TemporalAdjusters.lastDayOfMonth());
        
        return operationRepository.findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(id, start, end);
    }
}
