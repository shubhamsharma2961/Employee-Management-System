package com.company.ems.employee;
import org.mapstruct.*;

import com.company.ems.user.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

//	@Mapping(source = "company.id", target = "companyId")
//    @Mapping(source = "company.companyName", target = "companyName")
	
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")

    @Mapping(source = "designation.id", target = "designationId")
    @Mapping(source = "designation.name", target = "designationName")

    @Mapping(source = "employeeType.id", target = "employeeTypeId")
    @Mapping(source = "employeeType.name", target = "employeeTypeName")

    @Mapping(source = "reportingManager.id", target = "reportingManagerId")
    @Mapping(source = "reportingManager.fullName", target = "reportingManagerName")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(target = "roles", expression = "java(mapRoles(employee.getUser()))")
    EmployeeDto toDto(Employee employee);

    List<EmployeeDto> toDtoList(List<Employee> employees);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "designation", ignore = true)
    @Mapping(target = "employeeType", ignore = true)
    @Mapping(target = "reportingManager", ignore = true)
    @Mapping(target = "user", ignore = true)
    Employee toEntity(CreateEmployeeDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "designation", ignore = true)
    @Mapping(target = "employeeType", ignore = true)
    @Mapping(target = "reportingManager", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEmployeeFromDto(EditEmployeeDto dto, @MappingTarget Employee employee);
    
    default List<String> mapRoles(User user) {
        if (user == null || user.getUserRoles() == null) {
            return List.of();
        }
        return user.getUserRoles()
                .stream()
                .map(ur -> ur.getRole().getName())
                .toList();
    }
}