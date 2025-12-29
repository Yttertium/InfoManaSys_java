package com.ynu.service.impl;

import com.ynu.entity.Personnel;
import com.ynu.dto.Condition;
import com.ynu.repository.PersonnelRepository;
import com.ynu.service.PersonnelService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonnelServiceImpl implements PersonnelService {
    @Autowired
    private PersonnelRepository personnelRepository;

    @Override
    public void save(Personnel personnel) {
        personnelRepository.save(personnel);
    }

    @Override
    public void delete(Long id) {
        personnelRepository.deleteById(id);
    }

    @Override
    public void update(Personnel personnel) {
        personnelRepository.save(personnel);
    }

    // 简单查询实现
    @Override
    public List<Personnel> search(String name, String position) {
        Specification<Personnel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(position)) {
                predicates.add(cb.like(root.get("position"), "%" + position + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return personnelRepository.findAll(spec);
    }

    // 高级查询实现
    @Override
    public List<Personnel> searchAdvanced(List<Condition> conditions) {
        Specification<Personnel> spec = (root, query, cb) -> {
            // 无条件则查询所有
            if (conditions == null || conditions.isEmpty()) {
                return cb.conjunction();
            }

            Predicate resultPredicate = buildPredicate(conditions.get(0), root, cb);

            for (int i = 1; i < conditions.size(); i++) {
                Condition currentCond = conditions.get(i);

                Predicate nextPredicate = buildPredicate(currentCond, root, cb);

                // 如果当前条件无效跳过
                if (nextPredicate == null) {
                    continue;
                }

                // 如果之前的条件都无效，则当前条件直接作为起点
                if (resultPredicate == null) {
                    resultPredicate = nextPredicate;
                    continue;
                }

                // 根据 logic 字段判断是 AND 还是 OR
                if ("or".equalsIgnoreCase(currentCond.getLogic())) {
                    // (之前的所有结果) OR (当前条件)
                    resultPredicate = cb.or(resultPredicate, nextPredicate);
                } else {
                    // (之前的所有结果) AND (当前条件)
                    resultPredicate = cb.and(resultPredicate, nextPredicate);
                }
            }

            return resultPredicate != null ? resultPredicate : cb.conjunction();
        };

        return personnelRepository.findAll(spec);
    }


    // 根据单个 Condition 构建 JPA Predicate
    private Predicate buildPredicate(Condition c, Root<Personnel> root, CriteriaBuilder cb) {
        String field = c.getField();
        String val = c.getValue();
        String op = c.getOperator();

        // 如果字段名或值为空，视为无效条件
        if (!StringUtils.hasText(field) || !StringUtils.hasText(val)) {
            return null;
        }

        try {
            switch (op.toLowerCase()) {
                case "like": // 包含
                    return cb.like(root.get(field), "%" + val + "%");

                case "eq":   // 等于
                    if ("age".equals(field)) {
                        return cb.equal(root.get(field), Integer.parseInt(val));
                    } else {
                        return cb.equal(root.get(field), val);
                    }

                case "gt":   // 大于 (只针对 age)
                    if ("age".equals(field)) {
                        return cb.gt(root.get(field).as(Integer.class), Integer.parseInt(val));
                    }
                    return null; // 非数字字段不支持 gt

                case "lt":   // 小于 (只针对 age)
                    if ("age".equals(field)) {
                        return cb.lt(root.get(field).as(Integer.class), Integer.parseInt(val));
                    }
                    return null;

                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println("条件构建失败: " + e.getMessage());
            return null;
        }
    }
}
