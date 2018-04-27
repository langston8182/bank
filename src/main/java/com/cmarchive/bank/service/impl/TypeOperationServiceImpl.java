package com.cmarchive.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.domain.TypeOperation;
import com.cmarchive.bank.repository.TypeOperationRepository;
import com.cmarchive.bank.service.TypeOperationService;

@Service
public class TypeOperationServiceImpl implements TypeOperationService {

    private TypeOperationRepository typeOperationRepository;

    @Autowired
    public TypeOperationServiceImpl(TypeOperationRepository typeOperationRepository) {
        super();
        this.typeOperationRepository = typeOperationRepository;
    }
    
    @Override
    public Iterable<TypeOperation> list() {
        return typeOperationRepository.findAll();
    }
    
    @Override
    public TypeOperation getDebit() {
        return typeOperationRepository.findByValueType("DEBIT");
    }
    
    @Override
    public TypeOperation getCredit() {
        return typeOperationRepository.findByValueType("CREDIT");
    }
    
}
