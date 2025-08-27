package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeFacade;
import com.loopers.application.like.LikeProductCommand;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.RequireUserLoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
@RestController
public class LikeV1Controller implements LikeV1Spec {

	private final LikeFacade likeFacade;

	@PostMapping("/product/{productId}")
	@RequireUserLoginId
	@Override
	public ApiResponse<Object> likeProduct(
			@RequestHeader("X-USER-ID") String loginId,
			@PathVariable Long productId
	) {
		LikeProductCommand command = LikeProductCommand.of(loginId, productId);
		likeFacade.likeProduct(command);

		return ApiResponse.success();
	}

	@DeleteMapping("/product/{productId}")
	@RequireUserLoginId
	@Override
	public ApiResponse<Object> unlikeProduct(
			@RequestHeader("X-USER-ID") String loginId,
			@PathVariable Long productId
	) {
		LikeProductCommand command = LikeProductCommand.of(loginId, productId);
		likeFacade.unlikeProduct(command);

		return ApiResponse.success();
	}
}
