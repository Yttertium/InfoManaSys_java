package com.ynu.service;
import com.ynu.dto.Condition;
import com.ynu.entity.Personnel;
import java.util.List;

public interface PersonnelService {
    void save(Personnel personnel);
    void delete(Long id);
    void update(Personnel personnel);

    List<Personnel> search(String name, String position);
    List<Personnel> searchAdvanced(List<Condition> conditions);
}
