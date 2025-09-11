package com.loopers.application.like;

import com.loopers.EventPublisher;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.payload.like.ProductLikedEvent;
import com.loopers.payload.like.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeFacade {

	private final LikeService likeService;
	private final ProductService productService;
	private final UserService userService;
	private final EventPublisher eventPublisher;

	@Transactional
	public void likeProduct(LikeProductCommand command) {
		UserEntity user = userService.getUserByLoginId(command.loginId());
		ProductEntity product = productService.getProduct(command.productId());

		likeService.likeProduct(user.getId(), product.getId());
		eventPublisher.publish(ProductLikedEvent.of(product.getId()));
	}

	@Transactional
	public void unlikeProduct(LikeProductCommand command) {
		UserEntity user = userService.getUserByLoginId(command.loginId());
		ProductEntity product = productService.getProduct(command.productId());

		likeService.unlikeProduct(user.getId(), product.getId());
		eventPublisher.publish(ProductUnlikedEvent.of(product.getId()));
	}
}
