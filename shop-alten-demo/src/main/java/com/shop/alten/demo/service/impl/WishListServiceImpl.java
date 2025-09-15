package com.shop.alten.demo.service.impl;

import com.shop.alten.demo.dto.*;
import com.shop.alten.demo.entity.Product;
import com.shop.alten.demo.entity.Wishlist;
import com.shop.alten.demo.entity.WishlistItem;
import com.shop.alten.demo.exception.ResourceNotFoundException;
import com.shop.alten.demo.mapper.WishListMapper;
import com.shop.alten.demo.repository.ProductRepository;
import com.shop.alten.demo.repository.WishListRepository;
import com.shop.alten.demo.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.shop.alten.demo.enums.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    private final WishListMapper wishListMapper;

    private final ProductRepository productRepository;

    @Override
    public WishlistDto getWishListById(Long id) {
        return wishListRepository.findById(id)
                .map(wishListMapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public WishlistUpdateResponseDto updateWishList(Long id, WishlistDto wishlistDto) {
        Wishlist wishlist = wishListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist introuvable " + id));

        Map<Long, WishlistItem> oldItemsMap = mapOldWishListItems(wishlist);
        Set<Long> requestedProductIds = extractRequestedProductIds(wishlistDto);

        List<WishlistItemErrorDto> errors = new ArrayList<>();

        removeUnwantedwishlistItems(wishlist, requestedProductIds);
        addNewWishlistItemsInWishList(wishlist, oldItemsMap, requestedProductIds, errors);

        return WishlistUpdateResponseDto.builder()
                .wishlistDto(wishListMapper.toDTO(wishListRepository.save(wishlist)))
                .errors(errors)
                .build();
    }

    private Map<Long, WishlistItem> mapOldWishListItems(Wishlist wishlist) {
        return wishlist.getItems().stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), item -> item));
    }

    private Set<Long> extractRequestedProductIds(WishlistDto wishlistDto) {
        return wishlistDto.getWishlistItemDtoList().stream()
                .map(WishlistItemDto::getProductId)
                .collect(Collectors.toSet());
    }

    private void removeUnwantedwishlistItems(Wishlist wishlist, Set<Long> requestedProductIds) {
        wishlist.getItems()
                .removeIf(item -> !requestedProductIds.contains(item.getProduct().getId()));
    }

    private void addNewWishlistItemsInWishList(
            Wishlist wishlist,
            Map<Long, WishlistItem> oldItemsMap,
            Set<Long> requestedProductIds,
            List<WishlistItemErrorDto> errors
    ) {
        for (Long productId : requestedProductIds) {
            if (oldItemsMap.containsKey(productId)) continue;

            productRepository.findById(productId)
                    .ifPresentOrElse(
                            product -> wishlist.getItems().add(buildNewWishlistItem(product)),
                            () -> errors.add(buildProductNotFoundError(productId))
                    );
        }
    }

    private WishlistItem buildNewWishlistItem(Product product) {
        return WishlistItem.builder()
                .product(product)
                .addedAt(LocalDateTime.now())
                .build();
    }

    private WishlistItemErrorDto buildProductNotFoundError(Long productId) {
        return WishlistItemErrorDto.builder()
                .productId(productId)
                .codeErreur(PRODUCT_NOT_FOUND.name())
                .message("Produit plus disponible")
                .build();
    }
}