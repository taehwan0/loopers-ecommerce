package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeFacade {

	private final LikeService likeService;
	private final ProductService productService;
	private final UserService userService;

	public void likeProduct(LikeProductCommand command) {
		if (userService.getUser(command.userId()).isEmpty()) {
			throw new CoreException(ErrorType.NOT_FOUND, "[userId = " + command.userId() + "] 사용자를 찾을 수 없습니다.");
		}

		if (productService.getProduct(command.productId()).isEmpty()) {
			throw new CoreException(ErrorType.NOT_FOUND, "[productId = " + command.productId() + "] 상품을 찾을 수 없습니다.");
		}

		likeService.likeProduct(command.userId(), command.productId());
	}

	public void unlikeProduct(LikeProductCommand command) {
		if (userService.getUser(command.userId()).isEmpty()) {
			throw new CoreException(ErrorType.NOT_FOUND, "[userId = " + command.userId() + "] 사용자를 찾을 수 없습니다.");
		}

		if (productService.getProduct(command.productId()).isEmpty()) {
			throw new CoreException(ErrorType.NOT_FOUND, "[productId = " + command.productId() + "] 상품을 찾을 수 없습니다.");
		}

		likeService.unlikeProduct(command.userId(), command.productId());
	}
}
