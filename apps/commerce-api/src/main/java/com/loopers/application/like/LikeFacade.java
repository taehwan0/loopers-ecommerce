package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeFacade {

	private final LikeService likeService;
	private final ProductService productService;
	private final UserService userService;

	@Transactional
	public void likeProduct(LikeProductCommand command) {
		UserEntity user = userService.findByLoginId(command.loginId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + command.loginId() + "] 사용자를 찾을 수 없습니다."));

		ProductEntity product = productService.getProduct(command.productId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[productId = " + command.productId() + "] 상품을 찾을 수 없습니다."));

		likeService.likeProduct(user.getId(), product.getId());
	}

	@Transactional
	public void unlikeProduct(LikeProductCommand command) {
		UserEntity user = userService.findByLoginId(command.loginId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + command.loginId() + "] 사용자를 찾을 수 없습니다."));

		ProductEntity product = productService.getProduct(command.productId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[productId = " + command.productId() + "] 상품을 찾을 수 없습니다."));

		likeService.unlikeProduct(user.getId(), product.getId());
	}
}
