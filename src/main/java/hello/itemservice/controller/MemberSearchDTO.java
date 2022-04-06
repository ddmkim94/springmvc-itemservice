package hello.itemservice.controller;

import hello.itemservice.domain.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberSearchDTO {

    private Long id;
    private String name;

    private Address address;

    public MemberSearchDTO(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
