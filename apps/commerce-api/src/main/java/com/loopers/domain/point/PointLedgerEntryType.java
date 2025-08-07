package com.loopers.domain.point;

public enum PointLedgerEntryType {
	CHARGE, // 충전
	DEDUCT,    // 차감. 추후 사용과 소멸로 구분할 수 있음
	REFUND  // 환불
}
