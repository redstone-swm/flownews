package com.flownews.api.common.app

class NoDataException(message: String? = null) : RuntimeException(message ?: "데이터가 없습니다")