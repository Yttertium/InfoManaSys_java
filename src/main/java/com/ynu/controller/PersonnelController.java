package com.ynu.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynu.common.Result;
import com.ynu.entity.Personnel;
import com.ynu.dto.Condition;
import com.ynu.service.PersonnelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "人员信息管理")
@RestController
@RequestMapping("/api/person")
@CrossOrigin// 允许跨域
public class PersonnelController {
    @Autowired
    private PersonnelService personnelService;

    //解析 JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "条件查询人员列表")
    @GetMapping
    public Result<?> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "position", required = false) String position,
            // 接收前端的高级查询参数
            @RequestParam(name = "advancedConditions", required = false) String advancedConditions
    ) {
        List<Personnel> list;

        // 如果有 advancedConditions，优先走高级查询
        if (advancedConditions != null && !advancedConditions.isEmpty()) {
            try {
                // 1. 将 JSON 字符串解析为 Java List 对象
                List<Condition> conditions = objectMapper.readValue(
                        advancedConditions,
                        new TypeReference<>() {
                        }
                );
                // 2. 调用高级查询服务
                list = personnelService.searchAdvanced(conditions);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("查询条件格式错误");
            }
        } else {
            list = personnelService.search(name, position);
        }

        return Result.success(list);
    }

    @Operation(summary = "新增人员")
    @PostMapping
    public Result<?> save(@RequestBody Personnel personnel) {
        personnelService.save(personnel);
        return Result.success(null);
    }

    @Operation(summary = "修改人员")
    @PutMapping
    public Result<?> update(@RequestBody Personnel personnel) {
        personnelService.update(personnel);
        return Result.success(null);
    }

    @Operation(summary = "删除人员")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable(name = "id") Long id) {
        personnelService.delete(id);
        return Result.success(null);
    }
}
