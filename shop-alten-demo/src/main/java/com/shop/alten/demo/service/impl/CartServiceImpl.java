package com.shop.alten.demo.service.impl;

import com.shop.alten.demo.dto.CartDto;
import com.shop.alten.demo.dto.CartItemDto;
import com.shop.alten.demo.dto.CartItemErrorDto;
import com.shop.alten.demo.dto.CartUpdateResponseDto;
import com.shop.alten.demo.entity.Cart;
import com.shop.alten.demo.entity.CartItem;
import com.shop.alten.demo.entity.Product;
import com.shop.alten.demo.enums.InventoryStatus;
import com.shop.alten.demo.exception.ResourceNotFoundException;
import com.shop.alten.demo.mapper.CartMapper;
import com.shop.alten.demo.repository.CartRepository;
import com.shop.alten.demo.repository.ProductRepository;
import com.shop.alten.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shop.alten.demo.enums.ErrorStatus.INSUFFICIENT_STOCK;
import static com.shop.alten.demo.enums.ErrorStatus.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartUpdateResponseDto updateCart(Long idCart, CartDto cartDto) {
        List<CartItemErrorDto> productErrors = new ArrayList<>();

        Cart existingCart = getCartOrThrow(idCart);

        Map<Long, CartItem> oldCartItemsMap = mapOldItems(existingCart);
        Map<Long, CartItemDto> newCartItemsMap = mapNewItems(cartDto);
        //Traitement de la suppression des items qui sont absents ou quantité à zero
        handleRemovedItems(existingCart, newCartItemsMap);
        //Traitement modification de quantités des produits
        handleModifiedItemsQuantity(existingCart, oldCartItemsMap, cartDto.getCartItemDtoList(), productErrors);

        return CartUpdateResponseDto.builder()
                .cartDto(cartMapper.toDTO(cartRepository.save(existingCart)))
                .errors(productErrors)
                .build();
    }

    private Cart getCartOrThrow(Long idCart) {
        return cartRepository.findById(idCart)
                .orElseThrow(() -> new ResourceNotFoundException("Panier introuvable : " + idCart));
    }

    private Map<Long, CartItem> mapOldItems(Cart cart) {
        return cart.getItems().stream()
                .collect(Collectors.toMap(ci -> ci.getProduct().getId(), ci -> ci));
    }

    private Map<Long, CartItemDto> mapNewItems(CartDto cartDto) {
        return cartDto.getCartItemDtoList().stream()
                .collect(Collectors.toMap(CartItemDto::getProductId, ci -> ci));
    }

    private void handleRemovedItems(Cart existingCart, Map<Long, CartItemDto> newItemsMap) {
        for (CartItem oldCartItem : new ArrayList<>(existingCart.getItems())) {
            CartItemDto newItemDto = newItemsMap.get(oldCartItem.getProduct().getId());
            if (newItemDto == null || newItemDto.getQuantity() == 0) {
                Product product = oldCartItem.getProduct();
                product.setQuantity(product.getQuantity() + oldCartItem.getQuantity());
                updateInventoryStatus(product);
                productRepository.save(product);

                existingCart.getItems().remove(oldCartItem);
            }
        }
    }

    private void handleModifiedItemsQuantity(
            Cart existingCart,
            Map<Long, CartItem> oldCartItemsMap,
            List<CartItemDto> newItemsList,
            List<CartItemErrorDto> productErrors
    ) {
        // Ne garder que les produits avec une quantité > 0
        List<CartItemDto> validNewItems = newItemsList.stream()
                .filter(cartItemDto -> cartItemDto.getQuantity() > 0)
                .toList();

        for (CartItemDto newItemDto : validNewItems) {
            handleCartItem(existingCart, oldCartItemsMap, newItemDto, productErrors);
        }
    }

    private void handleCartItem(Cart existingCart, Map<Long, CartItem> oldCartItemsMap, CartItemDto newCartItemDto,
            List<CartItemErrorDto> productErrors) {
        // Vérifier la disponibilité du produit
        Optional<Product> optionalProduct = productRepository.findById(newCartItemDto.getProductId());
        if (optionalProduct.isEmpty()) {
            addProductNotFoundError(newCartItemDto, productErrors);
            return;
        }

        Product product = optionalProduct.get();
        CartItem oldCartItem = oldCartItemsMap.get(newCartItemDto.getProductId());

        if (oldCartItem == null) {
            handleNewCartItem(product, newCartItemDto.getQuantity(), existingCart, productErrors);
        } else {
            handleExistingCartItem(product, oldCartItem, newCartItemDto.getQuantity(), productErrors);
        }
    }

    private void addProductNotFoundError(CartItemDto newCartItemDto, List<CartItemErrorDto> productErrors) {
        productErrors.add(CartItemErrorDto.builder()
                .productId(newCartItemDto.getProductId())
                .codeErreur(PRODUCT_NOT_FOUND.name())
                .message("Produit plus disponible")
                .build());
    }

    private void handleNewCartItem(Product product, int newQuantity, Cart existingCart, List<CartItemErrorDto> productErrors) {
        if (product.getQuantity() < newQuantity) {
            productErrors.add(CartItemErrorDto.builder()
                    .productId(product.getId())
                    .codeErreur(INSUFFICIENT_STOCK.name())
                    .message("Stock insuffisant pour " + product.getName())
                    .build());
            return;
        }
        product.setQuantity(product.getQuantity() - newQuantity);
        updateInventoryStatus(product);
        productRepository.save(product);

        CartItem newCartItem = CartItem.builder()
                .product(product)
                .quantity(newQuantity)
                .build();
        existingCart.getItems().add(newCartItem);
    }

    private void handleExistingCartItem(Product product, CartItem oldCartItem, int newQuantity, List<CartItemErrorDto> productErrors) {
        int quantityDiff = newQuantity - oldCartItem.getQuantity();
        if (quantityDiff > 0) {
            if (product.getQuantity() < quantityDiff) {
                productErrors.add(CartItemErrorDto.builder()
                        .productId(product.getId())
                        .codeErreur(INSUFFICIENT_STOCK.name())
                        .message("Stock insuffisant pour " + product.getName())
                        .build());
                return;
            }
            product.setQuantity(product.getQuantity() - quantityDiff);
        } else if (quantityDiff < 0) {
            product.setQuantity(product.getQuantity() + Math.abs(quantityDiff));
        }
        updateInventoryStatus(product);
        productRepository.save(product);

        oldCartItem.setQuantity(newQuantity);
    }

    private void updateInventoryStatus(Product product) {
        if (product.getQuantity() == 0) {
            product.setInventoryStatus(InventoryStatus.OUTOFSTOCK);
        } else if (product.getQuantity() < 10) {
            product.setInventoryStatus(InventoryStatus.LOWSTOCK);
        } else {
            product.setInventoryStatus(InventoryStatus.INSTOCK);
        }
    }

    @Override
    public CartDto getCardById(Long id) {
        return cartRepository.findById(id)
                .map(cartMapper::toDTO)
                .orElse(null);
    }
}
