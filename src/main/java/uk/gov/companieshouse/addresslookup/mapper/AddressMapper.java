package uk.gov.companieshouse.addresslookup.mapper;

import org.mapstruct.Mapper;
import uk.gov.companieshouse.addresslookup.entity.AddressEntity;
import uk.gov.companieshouse.addresslookup.model.AddressDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    /**
     * Map AddressEntity to AddressDto
     */
    AddressDto entityToDto(AddressEntity entity);

    /**
     * Map AddressDto to AddressEntity
     */
    AddressEntity dtoToEntity(AddressDto dto);

    /**
     * Map list of AddressEntity to list of AddressDto
     */
    List<AddressDto> entitiesToDtos(List<AddressEntity> entities);

    /**
     * Map list of AddressDto to list of AddressEntity
     */
    List<AddressEntity> dtosToEntities(List<AddressDto> dtos);
}
