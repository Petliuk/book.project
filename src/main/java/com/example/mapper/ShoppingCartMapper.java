package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.cart.ShoppingCartDto;
import com.example.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "shoppingCart.user.id")
    @Mapping(target = "cartItems", source = "cartItems", qualifiedByName = "cartItemToDto")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
