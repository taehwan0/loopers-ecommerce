package com.loopers.application.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointAccountEntity;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointFacade {

	private final PointService pointService;
	private final UserService userService;

	@Transactional
	public PointInfo getUserPoint(String loginId) {
		UserEntity user = userService.findByLoginId(loginId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + loginId + "] 사용자를 찾을 수 없습니다."));

		PointAccountEntity pointAccount = pointService.getPointAccount(user.getId());

		return PointInfo.from(pointAccount);
	}

	@Transactional
	public PointInfo chargePoint(String loginId, long amount) {
		UserEntity userEntity = userService.findByLoginId(loginId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + loginId + "] 사용자를 찾을 수 없습니다."));

		PointAccountEntity pointAccount = pointService.chargePoint(userEntity.getId(), Point.of(amount));

		return PointInfo.from(pointAccount);
	}
}
