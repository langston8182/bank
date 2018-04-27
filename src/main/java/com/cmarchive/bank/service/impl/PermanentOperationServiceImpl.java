package com.cmarchive.bank.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.repository.PermanentOperationRepository;
import com.cmarchive.bank.service.PermanentOperationService;

@Service
public class PermanentOperationServiceImpl implements PermanentOperationService {

    private PermanentOperationRepository permanentOperationRepository;

    public PermanentOperationServiceImpl(PermanentOperationRepository permanentOperationRepository) {
        super();
        this.permanentOperationRepository = permanentOperationRepository;
    }
    
    @Override
    public PermanentOperation get(Long id) {
        return permanentOperationRepository.findOne(id);
    }
    
    @Override
    public List<PermanentOperation> list() {
        return permanentOperationRepository.findAllByOrderByJourAsc();
    }
    
    @Override
    public PermanentOperation save(PermanentOperation permanentOperation) {
        return permanentOperationRepository.save(permanentOperation);
    }
    
    @Override
    public void delete(Long id) {
        PermanentOperation permanentOperation = get(id);
        permanentOperation.getOperations().stream()
            .forEach(o -> o.setPermanentOperation(null));
        
        permanentOperationRepository.delete(id);
    }
    
    @Override
    public List<PermanentOperation> findByUser(Long id) {
        return permanentOperationRepository.findByUserId(id);
    }
}
