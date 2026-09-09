package uk.gov.companieshouse.addresslookup.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import uk.gov.companieshouse.addresslookup.entity.RoyalMailAddress;
import uk.gov.companieshouse.addresslookup.model.RoyalMailAddressDto;

@Mapper(componentModel = "spring")
public interface RoyalMailAddressMapper {

    RoyalMailAddressDto toDto(RoyalMailAddress address);

    List<RoyalMailAddressDto> toDtos(List<RoyalMailAddress> addresses);
}
