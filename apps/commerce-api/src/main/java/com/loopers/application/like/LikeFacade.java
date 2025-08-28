package com.loopers.application.like;

import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.shared.DomainEvent;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeFacade {

	private final LikeService likeService;
	private final ProductService productService;
	private final UserService userService;
	private final DomainEventPublisher eventPublisher;

	@Transactional
	public void likeProduct(LikeProductCommand command) {
		UserEntity user = userService.getUserByLoginId(command.loginId());
		ProductEntity product = productService.getProduct(command.productId());

		likeService.likeProduct(user.getId(), product.getId());
		eventPublisher.publish(DomainEvent.of(LikeEvent.ProductLike.of(user.getId(), product.getId())));
	}

	@Transactional
	public void unlikeProduct(LikeProductCommand command) {
		UserEntity user = userService.getUserByLoginId(command.loginId());
		ProductEntity product = productService.getProduct(command.productId());

		likeService.unlikeProduct(user.getId(), product.getId());
		eventPublisher.publish(DomainEvent.of(LikeEvent.ProductUnlike.of(user.getId(), product.getId())));
	}
}
